import service from "@/util/request";

const serverApi = {
    typeQuery: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/server/type/query',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    typeAdd: (data) => {
        return service({
            url: '/admin/api/server/type/add',
            method: 'post',
            data: data
        })
    },
    typeUpdate: (data) => {
        return service({
            url: '/admin/api/server/type/update',
            method: 'post',
            data: data
        })
    },
    typeSave: (data) => {
        if (data.id && data.id !== '') {
            return serverApi.typeUpdate(data)
        } else {
            return serverApi.typeAdd(data)
        }
    },
    typeChangeState: (ids, state) => {
        console.log(ids, state)
        return service({
            url: '/admin/api/server/type/change/state',
            method: 'post',
            data: {ids: ids, state: state}
        })
    },
    typeDelete: (ids) => {
        return service({
            url: '/admin/api/server/type/delete',
            method: 'post',
            data: {ids: ids}
        })
    },
    packageUpload: (formData) => {
        return service({
            url: '/admin/api/server/package/upload',
            method: 'post',
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            data: formData,
        })
    },
    hostQuery: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/server/host/query',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    hostAdd: (data) => {
        return service({
            url: '/admin/api/server/host/add',
            method: 'post',
            data: data
        })
    },
    hostUpdate: (data) => {
        return service({
            url: '/admin/api/server/host/update',
            method: 'post',
            data: data
        })
    },
    hostSave: (data) => {
        if (data.id && data.id !== '') {
            return serverApi.hostUpdate(data)
        } else {
            return serverApi.hostAdd(data)
        }
    },
    hostChangeState: (ids, state) => {
        console.log(ids, state)
        return service({
            url: '/admin/api/server/host/change/state',
            method: 'post',
            data: {ids: ids, state: state}
        })
    },
    hostDelete: (ids) => {
        return service({
            url: '/admin/api/server/host/delete',
            method: 'post',
            data: {ids: ids}
        })
    },
    configQuery: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/server/config/query',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    configAdd: (data) => {
        return service({
            url: '/admin/api/server/config/add',
            method: 'post',
            data: data
        })
    },
    configUpdate: (data) => {
        return service({
            url: '/admin/api/server/config/update',
            method: 'post',
            data: data
        })
    },
    configSave: (data) => {
        if (data.id && data.id !== '') {
            return serverApi.configUpdate(data)
        } else {
            return serverApi.configAdd(data)
        }
    },
    configChangeState: (ids, state) => {
        return service({
            url: '/admin/api/server/config/change/state',
            method: 'post',
            data: {ids: ids, state: state}
        })
    },
    configDelete: (ids) => {
        return service({
            url: '/admin/api/server/config/delete',
            method: 'post',
            data: {ids: ids}
        })
    },
    serviceList: (params) => {
        return service({
            url: '/admin/api/server/service/list',
            method: 'get',
            params: {...params}
        })
    },
    serviceQuery: (params) => {
        return service({
            url: '/admin/api/server/service/query',
            method: 'get',
            params: {...params}
        })
    },
    serviceDetect: (configId) => {
        return service({
            url: '/admin/api/server/service/detect',
            method: 'get',
            params: {configId: configId}
        })
    },
    servicePublish: (configId) => {
        return service({
            url: '/admin/api/server/service/publish',
            method: 'post',
            data: {configId: configId}
        })
    },
    serviceControl: (configId, operate) => {
        return service({
            url: '/admin/api/server/service/control',
            method: 'post',
            data: {configId: configId, operate: operate}
        })
    },
    getGateSetting: () => {
        return service({
            url: '/admin/api/server/gate/setting/get',
            method: 'get',
        })
    },
    saveGateSetting: (data) => {
        return service({
            url: '/admin/api/server/gate/setting/save',
            method: 'post',
            data: data,
        })
    }
}

export default serverApi