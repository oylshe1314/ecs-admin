<template>
    <el-dialog v-model="showEditRef" :title="titleRef" width="640px" @opened="handleOpened" @close="handleEditClose" destroy-on-close center>
        <el-form :model="formData" label-width="100px">
            <el-form-item label="角色" required>
                <el-select-v2 v-model="formData.roleId" :options="roleSelectOptionsRef" style="width: 240px"></el-select-v2>
            </el-form-item>
            <el-form-item label="用户名" required>
                <el-input v-model="formData.username" type="text" style="width: 240px"/>
            </el-form-item>
            <el-form-item label="密码" required>
                <el-input v-model="formData.password" type="text" style="width: 240px"/>
            </el-form-item>
            <el-form-item label="昵称" required>
                <el-input v-model="formData.nickname" type="text" style="width: 240px"/>
            </el-form-item>
            <el-form-item label="头像">
                <avatar-selector v-model="formData.avatar" :disabled="false"></avatar-selector>
            </el-form-item>
            <el-form-item label="邮箱">
                <el-input v-model="formData.email" type="text" style="width: 240px"/>
            </el-form-item>
            <el-form-item label="手机">
                <el-input v-model="formData.mobile" type="text" style="width: 240px"/>
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
    name: "AdminEdit"
}
</script>

<script setup>

import {ref, watch, reactive} from 'vue'

import {ElMessage, ElMessageBox} from "element-plus";

import commonApi from '@/api/common'
import adminApi from '@/api/admin'

import AvatarSelector from "@/components/AvatarSelector";

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true
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

const titleRef = ref('添加管理员')

const formData = reactive({
    id: '',
    roleId: '0',
    username: '',
    password: '123456',
    nickname: '',
    avatar: 'avatar1.png',
    email: '',
    mobile: '',
    remark: '',
})

const roleSelectOptionsRef = ref([{value: '0', label: '请选择角色'}])

function rolesToSelectOptions(roles) {
    let options = [{value: '0', label: '请选择角色'}]
    if (roles && Array.isArray(roles) && roles.length > 0) {
        roles.forEach((role) => options.push({value: role.id, label: role.name}))
    }
    return options
}

function init() {
    titleRef.value = '添加管理员'

    formData.id = ''
    formData.roleId = '0'
    formData.username = ''
    formData.password = '123456'
    formData.nickname = ''
    formData.avatar = 'avatar1.png'
    formData.email = ''
    formData.mobile = ''
    formData.remark = ''
}

watch(() => props.editData, (editData) => {
    if (editData == null) {
        init()
    } else {
        titleRef.value = '修改管理员'

        formData.id = editData.id
        formData.roleId = editData.roleId
        formData.username = editData.username
        formData.password = editData.password
        formData.nickname = editData.nickname
        formData.avatar = editData.avatar
        formData.email = editData.email
        formData.mobile = editData.mobile
        formData.remark = editData.remark
    }
})

function handleEditClose() {
    emits('update:modelValue', false)
}

function handleOpened() {
    commonApi.selectRoles()
        .then((res) => {
            roleSelectOptionsRef.value = rolesToSelectOptions(res)
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: '查询角色列表失败: ' + e.message})
        })
}

function handleSubmit() {
    console.log(formData)
    if (!formData.roleId || formData.roleId === '0') {
        ElMessage({type: 'error', showClose: true, message: '请选择角色'})
        return
    }
    if (!formData.username || formData.username === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入用户名'})
        return
    }
    if (formData.id && formData.id !== 0) {
        if (formData.password && formData.password === '') {
            formData.password = undefined
        }
    } else {
        if (!formData.password || formData.password === '') {
            ElMessage({type: 'error', showClose: true, message: '请输入密码'})
            return
        }
    }
    if (!formData.nickname || formData.nickname === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入昵称'})
        return
    }
    ElMessageBox.confirm('确认提交', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            adminApi.save(formData)
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
