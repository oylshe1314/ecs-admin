<template>
    <el-container>
        <el-header height="100px" class="top">
            <head-bar/>
        </el-header>
        <el-container>
            <el-aside width="200px">
                <left-menu/>
            </el-aside>
            <el-main>
                <main-view/>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
export default {
    name: 'IndexPage'
}
</script>

<script setup>

import HeadBar from '@/components/HeadBar.vue'
import LeftMenu from '@/components/LeftMenu.vue'
import MainView from '@/components/MainView'

</script>

<style scoped>

.el-header {
    margin: 0;
    padding: 0;
}

.el-main {
    margin: 0;
    padding: 0;
}

.el-container {
    height: 100%;
}

</style>
