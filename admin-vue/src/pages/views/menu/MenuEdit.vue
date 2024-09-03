<template>
    <el-dialog v-model="showEditRef" :title="titleRef" width="640px" @close="handleEditClose" destroy-on-close center>
        <el-form :model="formData" label-width="100px">
            <el-form-item label="类型" required>
                <el-select v-model.number="formData.type" @change="handleTypeChange" style="width: 240px">
                    <el-option :value='1' label="目录"/>
                    <el-option :value='2' label="菜单"/>
                    <el-option :value='3' label="接口"/>
                </el-select>
            </el-form-item>
            <el-form-item label="上级菜单">
                <el-select v-model="formData.parentId" placeholder="选择上级菜单" :disabled="parentSelectorDisableRef" style="width: 240px">
                    <el-option :value="'0'" label="选择上级菜单"/>
                    <el-option v-for="parent in parentSelectOptionsRef" :key="parent.id" :value='parent.id' :label="parent.name"/>
                </el-select>
            </el-form-item>
            <el-form-item label="图标">
                <icon-selector v-model="formData.icon" :disabled="iconSelectorDisabledRef" style="width: 240px"/>
            </el-form-item>
            <el-form-item label="名称" required>
                <el-input v-model="formData.name" type="text" style="width: 240px"/>
            </el-form-item>
            <el-form-item label="路径">
                <el-input v-model="formData.path" type="text" :disabled="pathDisabledRef" style="width: 100%"/>
            </el-form-item>
            <el-form-item label="排序值" required>
                <el-input-number v-model.number="formData.sortBy" :min="0" :max="9999" style="width: 100px" controls-position="right"></el-input-number>
            </el-form-item>
            <el-form-item label="备注">
                <el-input v-model="formData.remark" type="textarea" rows="4" style="width: 100%"/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="width: 80px" @click="close(false)">取消</el-button>
                <el-button type="primary" style="width: 80px" @click="handleSubmit">提交</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script>
export default {
    name: "MenuEdit"
}
</script>

<script setup>

import {reactive, ref, watch} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import commonApi from '@/api/common'
import menuApi from '@/api/menu'

import IconSelector from "@/components/IconSelector";

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true,
        default: () => false,
    },
    editData: {
        type: Object,
        default: () => null,
    }
})

const emits = defineEmits(['update:modelValue', 'editSuccess'])

const showEditRef = ref(props.modelValue)

watch(() => props.modelValue, (value) => {
    showEditRef.value = value
})

function close(success) {
    showEditRef.value = false
    if (success) {
        emits('editSuccess')
    }
}

const formData = reactive({
    id: '',
    type: 1,
    parentId: '0',
    icon: '',
    name: '',
    path: '',
    sortBy: 0,
    remark: '',
})

const titleRef = ref('菜单添加')

const parentSelectorDisableRef = ref(true)
const parentSelectOptionsRef = ref([])

const iconSelectorDisabledRef = ref(false)
const pathDisabledRef = ref(true)

const parentsMap = new Map();

function init() {
    titleRef.value = '菜单添加'

    formData.id = ''
    formData.type = 1
    formData.parentId = '0'
    formData.icon = ''
    formData.name = ''
    formData.path = ''
    formData.sortBy = 0
    formData.remark = ''

    handleTypeChange(formData.type)
}

watch(() => props.editData, (editData) => {
    if (editData == null) {
        init()
    } else {
        titleRef.value = '菜单修改'

        formData.id = editData.id
        formData.type = editData.type
        formData.parentId = editData.parentId
        formData.icon = editData.icon
        formData.name = editData.name
        formData.path = editData.path
        formData.sortBy = editData.sortBy
        formData.remark = editData.remark

        handleTypeChange(formData.type)
    }
})

function handleEditClose() {
    parentsMap.clear()
    emits('update:modelValue', false)
}

function queryParents(type) {
    const parents = parentsMap.get(type)
    if (!parents) {
        commonApi.selectMenus(type)
            .then((parents) => {
                parentSelectorDisableRef.value = false
                parentSelectOptionsRef.value = parents
                parentsMap.set(type, parents)
            })
            .catch((e) => {
                ElMessage({type: 'error', showClose: true, message: '查询上级菜单列表失败: ' + e.message})
            })
    } else {
        parentSelectorDisableRef.value = false
        parentSelectOptionsRef.value = parents
    }
}

function handleTypeChange(type) {
    switch (type) {
        case 1:
            parentSelectorDisableRef.value = true
            parentSelectOptionsRef.value = []
            iconSelectorDisabledRef.value = false
            pathDisabledRef.value = true

            formData.parentId = '0'
            if (props.editData) {
                formData.icon = props.editData.icon
            }
            break
        case 2:
            queryParents(1)
            iconSelectorDisabledRef.value = false
            pathDisabledRef.value = false
            if (props.editData && type === props.editData.type) {
                formData.parentId = props.editData.parentId
                formData.icon = props.editData.icon
            } else {
                formData.parentId = '0'
            }
            break
        case 3:
            queryParents(2)
            iconSelectorDisabledRef.value = true
            pathDisabledRef.value = false
            if (props.editData && type === props.editData.type) {
                formData.parentId = props.editData.parentId
            } else {
                formData.parentId = '0'
            }
            formData.icon = ''
            break
    }
}

function handleSubmit() {
    if (formData.type !== 1 && formData.type !== 2 && formData.type !== 3) {
        ElMessage({type: 'error', showClose: true, message: '请选择菜单类型'})
        return
    }
    if (formData.parentId === '0') {
        if (formData.type !== 1) {
            ElMessage({type: 'error', showClose: true, message: '请选择上级菜单'})
            return
        } else {
            formData.parentId = null
        }
    }
    if (formData.name === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入菜单名称'})
        return
    }
    if (formData.path === '' && formData.type !== 1) {
        ElMessage({type: 'error', showClose: true, message: '请输入菜单路径'})
        return
    }
    if (formData.sortBy < 1) {
        ElMessage({type: 'error', showClose: true, message: '排序值必须大于0'})
        return
    }

    ElMessageBox.confirm('确认提交', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            menuApi.save(formData)
                .then(() => {
                    close(true)
                    ElMessage({type: 'success', showClose: true, message: formData.id && formData.id !== '' ? '修改成功' : '添加成功'})
                    init()
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

</style>
