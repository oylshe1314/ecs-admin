import service from '@/util/request'

export default {

    changeDetail: (data) => {
        return service({
            url: '/admin/api/setting/change/detail',
            method: "post",
            data: data
        })
    },

    changePassword: (data) => {
        return service({
            url: '/admin/api/setting/change/password',
            method: "post",
            data: data
        })
    }
}
