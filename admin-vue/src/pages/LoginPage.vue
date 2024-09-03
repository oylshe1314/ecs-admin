<template>
    <div id="form" class="form">
        <el-form ref="formRef" :model="formData" @keyup.enter="handleLogin">
            <el-form-item prop="username">
                <el-input class="form-ipt" type="text" v-model="formData.username" :prefix-icon="User" size="large" maxlength="24">
                </el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input class="form-ipt" type="password" v-model="formData.password" :prefix-icon="Lock" size="large" maxlength="24" show-password/>
            </el-form-item>
            <el-form-item>
                <el-button class="form-ipt" type="primary" size="large" @click="handleLogin">登录</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
export default {
    name: 'LoginPage'
}
</script>

<script setup>

import {reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {User, Lock} from '@element-plus/icons-vue'

import router from "@/router";
import authApi from '@/api/auth'
import user from "@/store/user";

import {openLoading, closeLoading} from "@/util/loading";

const formRef = ref({})

// const formData = reactive({
//     username: 'admin',
//     password: 'shikeqing'
// })

const formData = reactive({
    username: '',
    password: ''
})

function handleLogin() {
    if (!formData.username || formData.username === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入账号'})
        return
    }
    if (!formData.password || formData.password === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入密码'})
        return
    }

    openLoading('#form', '正在登录...')
    authApi.login(formData).then(info => {
        user.setInfo(info)
        router.push({name: 'HomeView'})
    }).catch(e => {
        ElMessage({type: 'error', showClose: true, message: e.message})
    }).finally(() => {
        closeLoading()
    })
}
</script>

<style scoped>

.form-ipt {
    width: 320px;
    height: 40px;
}

.form {
    width: 320px;
    height: 156px;
    border-radius: 16px;
    /*background:  #a0cfff;*/
    position: absolute;
    top: 50%;
    left: 50%;
    margin: -118px 0 0 -200px;
    padding: 40px 40px 40px 40px;
}
</style>
