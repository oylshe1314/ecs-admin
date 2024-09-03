import axios from "axios";
import router from '@/router'
import tabs from '@/store/tabs'
import user from '@/store/user'

const service = axios.create({
    withCredentials: true
})

service.interceptors.response.use(
    (response) => {
        if (response.data.status === 200) {
            return Promise.resolve(response.data.data)
        } else {
            if (response.data.status === 4001) {
                tabs.clear()
                user.clear()
                router.push({name: 'LoginPage'})
            }
            return Promise.reject(new Error(response.data.message))
        }
    },
    (err) => {
        console.log('请求失败:', err.message)
        return Promise.reject(new Error('请求失败'))
    }
)

export default service
