<template>
    <div id="detail" class="detail">
        <div class="form">
            <el-form label-width="100px" ref="formRef" :model="formData" :role="formRules">
                <el-form-item label="角色">
                    <el-input v-model="formData.roleName" name="roleName" style="width: 240px" disabled></el-input>
                </el-form-item>
                <el-form-item label="用户名">
                    <el-input v-model="formData.username" name="username" style="width: 240px" disabled></el-input>
                </el-form-item>
                <el-form-item label="昵称">
                    <el-input v-model="formData.nickname" name="nickname" style="width: 240px" :disabled="editState.nickname"></el-input>
                </el-form-item>
                <el-form-item label="头像">
                    <avatar-selector v-model="formData.avatar" :disabled="editState.avatar"></avatar-selector>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="formData.email" name="email" style="width: 240px" :disabled="editState.email"></el-input>
                </el-form-item>
                <el-form-item label="手机">
                    <el-input v-model="formData.mobile" name="mobile" style="width: 240px" :disabled="editState.mobile"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleEditState" style="width: 80px">{{ editState.btnText }}</el-button>
                    <el-button v-show="editState.showSubmit" type="primary" style="width: 80px" @click="handleSubmit">提交</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
    name: 'DetailView'
}
</script>

<script setup>

import {ref, reactive} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";
import {closeLoading, openLoading} from "@/util/loading";

import user from '@/store/user'
import settingApi from '@/api/setting'

import AvatarSelector from "@/components/AvatarSelector";

const userInfo = user.getInfo

const editState = reactive({
    nickname: true,
    avatar: true,
    email: true,
    mobile: true,
    btnText: '修改',
    showSubmit: false,
})

const formRef = ref()

const formData = reactive({
    roleName: userInfo.roleName,
    username: userInfo.username,
    nickname: userInfo.nickname,
    avatar: userInfo.avatar,
    email: userInfo.email,
    mobile: userInfo.mobile,
})

function handleEditState() {
    editState.nickname = !editState.nickname
    editState.avatar = !editState.avatar
    editState.email = !editState.email
    editState.mobile = !editState.mobile
    editState.showSubmit = !editState.showSubmit
    editState.btnText = editState.showSubmit ? '取消' : '修改'
    if (!editState.showSubmit) {
        formData.roleName = userInfo.roleName
        formData.username = userInfo.username
        formData.nickname = userInfo.nickname
        formData.avatar = userInfo.avatar
        formData.email = userInfo.email
        formData.mobile = userInfo.mobile
    }
}

const formRules = reactive({
    nickname: [{required: true, trigger: 'blur', message: '昵称不能为空'}],
    avatar: [{
        required: true, trigger: 'blur', validator: (role, value, callback) => {
            if (value === '') {
                callback(new Error('请输入新密码'))
            } else if (value === formData.oldPassword) {
                callback(new Error('新密码不能与旧密码一致'))
            }
            callback()
        }
    }],
    email: [{
        required: true, trigger: 'blur', validator: (role, value, callback) => {
            if (value !== '') {
                console.log(value)
            }
            callback()
        }
    }],
    mobile: [{
        required: true, trigger: 'blur', validator: (role, value, callback) => {
            if (value !== '') {
                console.log(value)
            }
            callback()
        }
    }],
})

function handleSubmit() {

    formRef.value.validate().then((ok) => {
        if (ok) {
            ElMessageBox.confirm('确认提交修改', '警告', {confirmButtonText: '确认', cancelButtonText: '取消'})
                .then(() => {
                    openLoading('#detail', '已提交，请稍候...')
                    settingApi.changeDetail(formData).then(() => {

                        userInfo.nickname = formData.nickname
                        userInfo.avatar = formData.avatar
                        userInfo.email = formData.email
                        userInfo.mobile = formData.mobile

                        handleEditState()
                        ElMessage({type: 'success', showClose: true, message: '修改成功'})
                    }).catch((e) => {
                        ElMessage({type: 'error', showClose: true, message: e.message})
                    }).finally(() => {
                        closeLoading()
                    })
                })
                .catch(() => {
                })
        }
    }).catch((err) => {
        if (err.oldPassword) {
            ElMessage({type: 'error', showClose: true, message: err.oldPassword[0].message})
        } else if (err.newPassword) {
            ElMessage({type: 'error', showClose: true, message: err.newPassword[0].message})
        } else if (err.confirmPassword) {
            ElMessage({type: 'error', showClose: true, message: err.confirmPassword[0].message})
        }
    })
}

</script>

<style scoped>

.detail {
    width: 100%;
    height: 100%;
    /*display: flex;*/
    /*justify-content: center;*/
}

.form {
    width: 680px;
    height: 642px;
    /*background: pink;*/
    /*position: relative;*/
    /*top: 200px;*/
    /*left: 50%;*/
    /*margin: 180px 0 0 -180px;*/
    padding: 40px 40px 40px 40px;
}

.avatar-select {
    width: 500px;
    height: 250px;
    /*background: pink;*/
}

.avatar-select ul {
    display: block;
    margin: 0;
    padding: 0;
}

.avatar-select li {
    display: block;
    margin-right: 5px;
    padding: 0;
    float: left;
    text-align: center;
    cursor: pointer;
}

.avatar-item {
    width: 120px;
    height: 120px;
}

.avatar-item:active {
    width: 119px;
    height: 119px;
    /*border: #409EFF solid 1px;*/
}

</style>
