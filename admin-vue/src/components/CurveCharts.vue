<template>
    <div>
        <canvas ref="chartsRef" :width="widthRef" :height="heightRef"></canvas>
    </div>
</template>

<script setup>

import {ref, watch, onUpdated} from 'vue'

const props = defineProps({
    width: {
        type: Number,
        required: true
    },
    height: {
        type: Number,
        required: true
    },
    labels: {
        type: Array,
        required: true
    },
    data: {
        type: Array,
        required: true
    }
})

const chartsRef = ref()
const widthRef = ref(props.width)
const heightRef = ref(props.height)

const labelsRef = ref(props.labels)
const dataRef = ref(props.data)

function dataRange(data, range) {
    let min = data[0]
    let max = data[0]

    for (let i = 1; i < data.length; i++) {
        if (data[i] < min) {
            min = data[i]
        }
        if (data[i] > max) {
            max = data[i]
        }
    }

    if (max === 0) {
        range.push(6, 5, 4, 3, 2, 1, 0)
        return
    }

    if (min === max) {
        min = 0
    }

    const size = Math.floor(Math.log10(max)) + 1
    let rangeMax = Math.floor(Math.pow(10, size) * 1.2)
    if (max <= rangeMax / 2) {
        rangeMax /= 2
    }

    const iv = Math.floor(rangeMax / 6)

    let rangeMin = 0
    if (min > iv) {
        if (max - min > iv) {
            rangeMin = iv * Math.floor(min / iv)
            rangeMax += rangeMin
        }
    } else if (min < 0) {
        let dn = Math.floor(Math.abs(min) / iv)
        if (Math.abs(min) % iv > 0) {
            dn += 1
        }
        rangeMin = -(iv * dn)
        if (max > rangeMax + rangeMin) {
            rangeMin = -rangeMax
        } else {
            rangeMax += rangeMin
        }
    }

    for (let i = rangeMax; i >= rangeMin; i -= iv) {
        range.push(i)
    }
}

function draw(labels, data) {
    const width = chartsRef.value.width
    const height = chartsRef.value.height
    const ctx = chartsRef.value.getContext('2d')

    ctx.clearRect(0, 0, width, height)

    ctx.lineWidth = 2
    ctx.lineCap = 'round'
    ctx.strokeStyle = 'blue'

    ctx.beginPath()
    ctx.moveTo(100, 0)
    ctx.lineTo(100, height - 20)
    ctx.lineTo(width, height - 20)
    ctx.stroke()

    const range = []
    dataRange(data, range)

    const rowHeight = Math.floor((height - 20) / 6)

    ctx.font = '12px serif'
    ctx.textAlign = 'right'
    ctx.fillStyle = 'blue'
    ctx.lineWidth = 1
    ctx.strokeStyle = 'gray'
    ctx.beginPath()
    for (let i = 1; i <= 6; i++) {
        ctx.fillText(String(range[i]), 90, (rowHeight * i) + 4 + 0.5)
        if (i < 6) {
            ctx.moveTo(100, rowHeight * i + 0.5)
            ctx.lineTo(width, rowHeight * i + 0.5)
        }
    }
    ctx.stroke()

    ctx.font = '12px serif'
    ctx.textAlign = 'left'
    ctx.fillStyle = 'blue'

    ctx.lineWidth = 1
    ctx.strokeStyle = 'blue'

    const columnWidth = Math.floor((width - 100) / (labels.length + 1))

    ctx.beginPath()
    for (let i = 0; i < labels.length; i++) {
        const columnX = (100 + columnWidth * (i + 1))
        ctx.fillText(labels[i], columnX - (labels[i].length * 8 / 2), height - 6 + 0.5)
        ctx.moveTo(columnX, height - 20 + 0.5)
        ctx.lineTo(columnX, height - 25 + 0.5)
    }
    ctx.stroke()

    ctx.strokeStyle = 'red'
    ctx.lineWidth = 1
    ctx.beginPath()
    for (let i = 0; i < data.length; i++) {
        let columnX = (100 + columnWidth * (i + 1))
        let columnY = (height) - ((height - 20) / range[0] * data[i]) - 22
        if (i === 0) {
            ctx.moveTo(columnX, columnY)
        } else {
            ctx.lineTo(columnX, columnY)
        }
    }
    ctx.stroke()
}

watch(() => props.labels, (labels) => {
    labelsRef.value = labels
})

watch(() => props.data, (data) => {
    dataRef.value = data
})

onUpdated(() => {
    draw(labelsRef.value, dataRef.value)
})

</script>

<style scoped>

</style>