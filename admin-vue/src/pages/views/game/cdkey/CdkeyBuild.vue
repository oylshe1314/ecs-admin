<template>
    <el-dialog v-model="showEditRef" :title="titleRef" width="640px" @close="handleEditClose" destroy-on-close center>
        <el-form :model="formData" label-width="100px">
            <el-form-item label="目录渠道">
                <el-select v-model.number="formData.channel" placeholder="渠道" style="width: 240px">
                    <el-option :value="0" label="所有渠道"/>
                    <el-option :value="1" label="微信小游戏"/>
                    <el-option :value="2" label="TapTap"/>
                </el-select>
            </el-form-item>
            <template v-if="buildTypeRef==='generate'">
                <el-form-item label="生成数量" required>
                    <el-input v-model.number="formData.count" type="text" style="width: 240px"/>
                </el-form-item>
            </template>
            <template v-else>
                <el-form-item label="兑换码" required>
                    <el-input v-model="formData.key" type="text" style="width: 240px"/>
                </el-form-item>
            </template>
            <el-form-item label="有效天数">
                <el-input-number v-model.number="formData.validDays" :min="0" :max="9999" style="width: 100px" controls-position="right"></el-input-number>
                <span style="width: 10px;"/><span style="color: #ff4949;">填0为永久有效。</span>
            </el-form-item>
            <el-form-item label="奖励物品" required>
                <el-input v-model="formData.itemId" type="textarea" rows="4" style="width: 100%" placeholder="奖励物品ID，多个物品用英文逗号隔开。"/>
            </el-form-item>
            <el-form-item label="物品数量" required>
                <el-input v-model="formData.itemNum" type="textarea" rows="4" style="width: 100%"
                          placeholder="奖励物品数量，多个物品的数量用英文逗号隔开且个数需与ID个数一致。"/>
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
    name: "CdkeyBuild"
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
    },
    buildType: {
        type: String,
        default: () => 'add',
    }
})

const emits = defineEmits(['update:modelValue', 'editSuccess'])

const showEditRef = ref(props.modelValue)
const buildTypeRef = ref(props.buildType)

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
    channel: 0,
    key: '',
    count: 100,
    validDays: 0,
    itemId: '',
    itemNum: '',
})

const titleRef = ref('兑换码添加')

function init() {
    titleRef.value = buildTypeRef.value === 'generate' ? '兑换码生成' : '兑换码添加'

    formData.channel = 0
    formData.key = ''
    formData.count = 100
    formData.validDays = 0
    formData.itemId = ''
    formData.itemNum = ''
}

watch(() => props.buildType, (buildType) => {
    buildTypeRef.value = buildType
    init()
})

function handleEditClose() {
    emits('update:modelValue', false)
}

function handleSubmit() {
    if (buildTypeRef.value === 'generate') {
        if (!formData.count || formData.count === '') {
            ElMessage({type: 'error', showClose: true, message: '请输入生成数量'})
            return
        }
        formData.count = Number(formData.count)
    } else {
        if (!formData.key || formData.key === '') {
            ElMessage({type: 'error', showClose: true, message: '请输入兑换码'})
            return
        }
    }
    if (!formData.itemId || formData.itemId === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入奖励物品'})
        return
    }
    if (!formData.itemNum || formData.itemNum === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入物品数量'})
        return
    }

    const data = {channel: 0, validDays: 0}
    if (formData.channel && formData.channel > 0) {
        data.channel = formData.channel
    }
    if (buildTypeRef.value === 'generate') {
        data.count = formData.count
    } else {
        data.key = formData.key
    }
    if (formData.validDays && formData.validDays > 0) {
        data.validDays = formData.validDays
    }

    data.itemId = formData.itemId.split(',').map(Number)
    data.itemNum = formData.itemNum.split(',').map(Number)

    if (data.itemNum.length !== data.itemId.length) {
        ElMessage({type: 'error', showClose: true, message: '奖励物品或者数量不正确'})
        return
    }

    ElMessageBox.confirm('确认提交', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            if (buildTypeRef.value === 'generate') {
                gameApi.cdkeyGenerate(data)
                    .then(() => {
                        close(true)
                        ElMessage({type: 'success', showClose: true, message: '生成成功'})
                        init()
                    })
                    .catch((e) => {
                        ElMessage({type: 'error', showClose: true, message: e.message})
                    })
            } else {
                gameApi.cdkeyAdd(data)
                    .then(() => {
                        close(true)
                        ElMessage({type: 'success', showClose: true, message: '添加成功'})
                        init()
                    })
                    .catch((e) => {
                        ElMessage({type: 'error', showClose: true, message: e.message})
                    })
            }
        })
        .catch(() => {
        })
}

</script>

<style scoped>

</style>
