<template>
    <el-select v-model="modelValueRef" placeholder="选择图标" :disabled="disabled" @clear="handleSelectClear" @cancel="handleCancel" clearable>
        <template v-if="modelValueRef !== ''" #prefix>
            <el-icon size="18" color="#606266">
                <component :is="modelValueRef"/>
            </el-icon>
        </template>
        <el-option class="icon-option" :value="modelValueRef">
            <div>
                <ul>
                    <li v-for="icon in proxy.$icons" :key="icon">
                        <el-icon size="20" @click="handleIconClick(icon)">
                            <component :is="icon"/>
                        </el-icon>
                    </li>
                </ul>
            </div>
        </el-option>
    </el-select>
</template>

<script setup>

import {ref, watch, getCurrentInstance} from 'vue'

const props = defineProps({
    modelValue: {
        type: String,
        required: true
    },
    disabled: {
        type: Boolean,
        default: () => false,
    }
})

const {proxy} = getCurrentInstance()

const modelValueRef = ref(props.modelValue)

watch(() => props.modelValue, (modelValue) => {
    modelValueRef.value = modelValue
})

const emits = defineEmits(['update:modelValue'])

function handleIconClick(icon) {
    modelValueRef.value = icon
    emits('update:modelValue', modelValueRef.value)
}

function handleSelectClear() {
    modelValueRef.value = ''
    emits('update:modelValue', modelValueRef.value)
}

function handleCancel() {
    modelValueRef.value = ''
    emits('update:modelValue', modelValueRef.value)
}

</script>

<style scoped>

.icon-option {
    width: 320px;
    height: auto;
    max-height: 200px;
    overflow-y: auto;
    padding: 0;
}

.icon-option, .icon-option:hover {
    background-color: #fff;
    cursor: default;
}

.icon-option {
    color: #606266;
    font-weight: normal;
}

.icon-option::-webkit-scrollbar {
    width: 8px;
    background-color: #eee;
}

.icon-option::-webkit-scrollbar-track {
    background-color: #eee;
}

.icon-option::-webkit-scrollbar-thumb {
    border-radius: 4px;
    background: #d7d7d7;
}

.icon-option > div {
    cursor: default;
}

.icon-option ul {
    display: block;
    margin: 0;
    padding: 0;
}

.icon-option ul > li {
    display: block;
    margin: 0;
    padding: 0;
    float: left;
    text-align: center;
    cursor: pointer;
    width: 30px;
    height: 30px;
    line-height: 30px;
}

.icon-option ul > li:hover {
    color: #409EFF;
}

</style>
