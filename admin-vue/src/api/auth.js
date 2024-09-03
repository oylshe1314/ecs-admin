import service from '@/util/request'

export default {
    login: (data) => {
        return service({
            url: '/admin/api/auth/login',
            method: "post",
            data: data
        })
    },
    logout: () => {
        return service({
            url: '/admin/api/auth/logout',
            method: "post",
        })
    }
}
