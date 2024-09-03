import {defineStore} from 'pinia'

const userStore = defineStore({
    id: 'user',
    state: () => {
        const value = sessionStorage.getItem('user.info')
        if (value && value !== '') {
            return {info: JSON.parse(value)}
        } else {
            return {info: null}
        }
    },
    actions: {
        setInfo(info) {
            this.info = info
        },
        clear() {
            sessionStorage.removeItem('user.info')
            this.$reset()
        }
    },
    getters: {
        getInfo() {
            return this.info
        }
    }
})

export default userStore
