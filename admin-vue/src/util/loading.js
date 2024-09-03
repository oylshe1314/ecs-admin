import {ElLoading} from "element-plus";
import {ref} from "vue";

const loading = ref()

export function openLoading(target, text) {
    loading.value = ElLoading.service({
        target: target,
        text: (!text || text === '') ? '处理中，请稍候' : text,
    })
}

export function closeLoading() {
    loading.value.close()
    loading.value = false
}
