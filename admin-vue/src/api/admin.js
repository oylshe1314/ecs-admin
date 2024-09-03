import service from '@/util/request'

const adminApi = {
    roleQuery: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/admin/role/query',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    roleAdd: (data) => {
        return service({
            url: '/admin/api/admin/role/add',
            method: 'post',
            data: data
        })
    },
    roleUpdate: (data) => {
        return service({
            url: '/admin/api/admin/role/update',
            method: 'post',
            data: data
        })
    },
    roleSave: (data) => {
        if (data.id && data.id !== '') {
            return adminApi.roleUpdate(data)
        } else {
            return adminApi.roleAdd(data)
        }
    },
    roleChangeState: (ids, state) => {
        console.log(ids, state)
        return service({
            url: '/admin/api/admin/role/change/state',
            method: 'post',
            data: {ids: ids, state: state}
        })
    },
    roleDelete: (ids) => {
        return service({
            url: '/admin/api/admin/role/delete',
            method: 'post',
            data: {ids: ids}
        })
    },
    roleMenuQuery: (roleId) => {
        return service({
            url: '/admin/api/admin/role/menu/query',
            method: 'get',
            params: {roleId: roleId}
        })
    },
    roleMenuSet: (roleId, menuIds) => {
        return service({
            url: '/admin/api/admin/role/menu/set',
            method: 'post',
            data: {roleId: roleId, menuIds: menuIds}
        })
    },
    query: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/admin/query',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    add: (data) => {
        return service({
            url: '/admin/api/admin/add',
            method: 'post',
            data: data
        })
    },
    update: (data) => {
        return service({
            url: '/admin/api/admin/update',
            method: 'post',
            data: data
        })
    },
    save: (data) => {
        if (data.id && data.id !== '') {
            return adminApi.update(data)
        } else {
            return adminApi.add(data)
        }
    },
    changeState: (ids, state) => {
        return service({
            url: '/admin/api/admin/change/state',
            method: 'post',
            data: {ids: ids, state: state}
        })
    },
    delete: (ids) => {
        return service({
            url: '/admin/api/admin/delete',
            method: 'post',
            data: {ids: ids}
        })
    },
}

export default adminApi
