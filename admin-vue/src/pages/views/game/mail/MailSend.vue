<template>
    <el-dialog v-model="showEditRef" :title="titleRef" width="640px" @close="handleEditClose" destroy-on-close center>
        <el-form :model="formData" label-width="100px">
            <el-form-item label="服务器ID">
                <el-input v-model="formData.appIds" type="text" style="width: 100%"
                          placeholder="目标服务器ID，多个服务器用英文逗号隔开，不填则为全部服务器。"></el-input>
            </el-form-item>
            <el-form-item label="目录玩家">
                <el-input v-model="formData.playerIds" type="textarea" rows="2" style="width: 100%"
                          placeholder="目标玩家ID，多个玩家用英文逗号隔开，不填则为全服邮件。"/>
            </el-form-item>
            <el-form-item label="邮件标题" required>
                <el-input v-model="formData.title" type="text" style="width: 100%" placeholder="邮件标题"></el-input>
            </el-form-item>
            <el-form-item label="邮件内容" required>
                <el-input v-model="formData.content" type="textarea" rows="8" style="width: 100%" placeholder="邮件内容"/>
            </el-form-item>
            <el-form-item label="奖励物品">
                <el-input v-model="formData.itemId" type="textarea" rows="2" style="width: 100%" placeholder="奖励物品ID，多个物品用英文逗号隔开。"/>
            </el-form-item>
            <el-form-item label="物品数量">
                <el-input v-model="formData.itemNum" type="textarea" rows="2" style="width: 100%"
                          placeholder="奖励物品数量，多个物品的数量用英文逗号隔开且个数需与ID个数一致。"/>
            </el-form-item>
            <el-form-item label="有效天数">
                <el-input-number v-model.number="formData.expiration" :min="0" :max="999" controls-position="right" style="width: 120px"></el-input-number>
                <span style="width: 10px;"/><span style="color: #ff4949;">有效期默认30天，填0为永久有效。</span>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="width: 80px" @click="close(false)">取消</el-button>
                <el-button type="primary" style="width: 80px" @click="handleSubmit">发送</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script>
export default {
    name: "MailSend"
}
</script>

<script setup>

import {reactive, ref, watch} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import gameApi from '@/api/game'

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true,
        default: () => false,
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
    appIds: '',
    playerIds: '',
    title: '',
    content: '',
    args: '',
    itemId: '',
    itemNum: '',
    expiration: 30,
})

function init() {
    formData.appIds = ''
    formData.playerIds = ''
    formData.title = ''
    formData.content = ''
    formData.itemId = ''
    formData.itemNum = ''
    formData.expiration = 30
}

const titleRef = ref('邮件发送')

function handleEditClose() {
    emits('update:modelValue', false)
}

function handleSubmit() {
    if (formData.title === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入邮件标题'})
        return
    }
    if (formData.content === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入邮件内容'})
        return
    }

    const data = {title: formData.title, content: formData.content, expiration: Number(formData.expiration)}
    if (formData.appIds !== '') {
        data.appIds = formData.appIds.split(',').map(Number)
    }
    if (formData.playerIds !== '') {
        data.playerIds = formData.playerIds.split(',').map(Number)
    }
    if (formData.itemId !== '') {
        data.itemId = formData.itemId.split(',').map(Number)
    }
    if (formData.itemNum !== '') {
        data.itemNum = formData.itemNum.split(',').map(Number)
    }

    if (data.itemNum.length !== data.itemId.length) {
        ElMessage({type: 'error', showClose: true, message: '奖励物品或者数量不正确'})
        return
    }

    ElMessageBox.confirm('确认提交', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            gameApi.mailSend(data)
                .then(() => {
                    close(true)
                    init()
                    ElMessage({type: 'success', showClose: true, message: '发送成功'})
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