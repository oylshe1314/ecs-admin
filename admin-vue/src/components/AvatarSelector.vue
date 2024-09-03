<template>
    <el-popover ref="popoverRef" placement="right-start" width="530" trigger="click" :disabled="disabledRef">
        <div class="avatar-select">
            <ul>
                <li v-for="(avatar, index) in avatars" :key="index">
                    <div style="width: 120px; height: 120px;">
                        <img alt="" class="avatar-item" :src="require('@/assets/images/avatars/' + avatar)" @click="handleAvatarSelect(avatar)">
                    </div>
                </li>
            </ul>
        </div>
        <template #reference>
            <div style="width: 120px; height: 120px;">
                <img alt="" class="avatar-item" :src="require('@/assets/images/avatars/' + modelValueRef)"/>
            </div>
        </template>
    </el-popover>
</template>

<script setup>

import {ref, watch} from 'vue'

const avatars = [
    'avatar1.png',
    'avatar2.png',
    'avatar3.png',
    'avatar4.png',
    'avatar5.png',
    'avatar6.png',
    'avatar7.png',
    'avatar8.png',
]

const props = defineProps({
    modelValue: {
        type: String,
        required: true,
    },
    disabled: {
        type: Boolean,
        default: () => false
    }
})

const popoverRef = ref()

const modelValueRef = ref((!props.modelValue || props.modelValue === '') ? avatars[0] : props.modelValue)

watch(() => props.modelValue, (value) => {
    if (!value || value === '') {
        modelValueRef.value = avatars[0]
    } else {
        modelValueRef.value = value
    }
})

const disabledRef = ref(props.disabled)

watch(() => props.disabled, (disabled) => {
    disabledRef.value = disabled
})

const emits = defineEmits(['update:modelValue'])

function handleAvatarSelect(avatar) {
    emits('update:modelValue', avatar)
    popoverRef.value.hide()
}

</script>

<style scoped>

.avatar-select {
    width: 500px;
    height: 250px;
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
