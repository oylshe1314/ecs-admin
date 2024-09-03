<template>
    <el-dialog v-model="showEditRef" title="角色菜单管理" width="640px" @close="handleClose" destroy-on-close center>
        <div style="height: 600px">
            <el-tree-v2 ref="treeRef"
                        v-model="treeDataRef"
                        :data="treeDataRef"
                        :props="treeProps"
                        :default-checked-keys="treeCheckedKeysRef"
                        :height="572"
                        class="menu-tree"
                        show-checkbox>
            </el-tree-v2>
        </div>
        <div style="width: 100%; display: flex; justify-content: center">
            <el-button type="primary" style="width: 80px" @click="close">取消</el-button>
            <el-button type="primary" style="width: 80px" @click="handleSubmit">提交</el-button>
        </div>
    </el-dialog>
</template>

<script>
export default {
    name: "RoleMenus"
}
</script>

<script setup>

import {ref, watch} from 'vue'

import {ElMessage, ElMessageBox} from "element-plus";

import adminApi from '@/api/admin'

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true,
    },
    editData: {
        type: Object,
        required: true,
        default: () => {
            return {roleId: 0, menus: []}
        }
    }
})

const showEditRef = ref(false)

watch(() => props.modelValue, (value) => {
    showEditRef.value = value
})

function close() {
    showEditRef.value = false
}

const emits = defineEmits(['update:modelValue'])

function handleClose() {
    treeDataRef.value = []
    treeCheckedKeysRef.value = []
    emits('update:modelValue', false)
}

const treeProps = {
    value: 'id',
    label: 'label',
    children: 'children',
}
const treeRef = ref()
const treeDataRef = ref([])
const treeCheckedKeysRef = ref([])

function menusToTreeNodes(parent, menus, checkedKeys) {
    const nodes = []
    if (menus && Array.isArray(menus) && menus.length > 0) {
        menus.forEach((menu) => {
            const node = {
                id: menu.id,
                label: menu.name,
                disabled: props.editData.roleId === 1 && menu.id < 28
            }
            node.children = menusToTreeNodes(menu, menu.subMenus, checkedKeys)
            nodes.push(node)
            if (menu.checked) {
                checkedKeys.push(menu.id)
            } else {
                if (parent) {
                    parent.checked = false
                }
            }
        })
    }
    return nodes
}

watch(() => props.editData, (editData) => {
    const checkedKeys = []
    treeDataRef.value = menusToTreeNodes(null, editData.menus, checkedKeys)
    treeCheckedKeysRef.value = checkedKeys
})

function handleSubmit() {
    const menuIds = []
    menuIds.push(...treeRef.value.getHalfCheckedKeys())
    menuIds.push(...treeRef.value.getCheckedKeys())

    ElMessageBox.confirm('确认保存角色菜单', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            adminApi.roleMenuSet(props.editData.roleId, menuIds)
                .then(() => {
                    close()
                    ElMessage({type: 'success', showClose: true, message: '设置成功'})
                })
                .catch((e) => {
                    ElMessage({type: 'error', showClose: true, message: e.message})
                })
        })
        .catch(() => {
        })
}

</script>

<style scoped>

.menu-tree {
    width: 100%;
    height: 572px;
    border: #e9e9eb solid 1px;
    border-radius: 5px;
}

</style>
