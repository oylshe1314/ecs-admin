import service from '@/util/request'

export default {
    roleMenus: () => {
        return service({
            url: '/admin/api/common/role/menus',
            method: "get",
        })
    },
    selectRoles: () => {
        return service({
            url: '/admin/api/common/select/roles',
            method: 'get',
        })
    },
    selectMenus: (type) => {
        return service({
            url: '/admin/api/common/select/menus',
            method: 'get',
            params: {type: type}
        })
    },
    selectServerTypes: () => {
        return service({
            url: '/admin/api/common/select/server/types',
            method: 'get',
        })
    },
    selectServerHosts: () => {
        return service({
            url: '/admin/api/common/select/server/hosts',
            method: 'get',
        })
    },
}
