import service from '@/util/request'

const menuApi = {
    query: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/menu/query',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    add: (data) => {
        return service({
            url: '/admin/api/menu/add',
            method: 'post',
            data: data
        })
    },
    update: (data) => {
        return service({
            url: '/admin/api/menu/update',
            method: 'post',
            data: data
        })
    },
    save: (data) => {
        if (data.id && data.id !== '') {
            return menuApi.update(data)
        } else {
            return menuApi.add(data)
        }
    },
    changeState: (ids, state) => {
        return service({
            url: '/admin/api/menu/change/state',
            method: 'post',
            data: {ids: ids, state: state}
        })
    },
    delete: (ids) => {
        return service({
            url: '/admin/api/menu/delete',
            method: 'post',
            data: {ids: ids}
        })
    },
}

export default menuApi
