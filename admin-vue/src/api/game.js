import service from "@/util/request";

const gameApi = {
    mailList: (appId) => {
        return service({
            url: '/admin/api/game/mail/list',
            method: 'get',
            params: {appId: appId}
        })
    },
    mailSend: (data) => {
        return service({
            url: '/admin/api/game/mail/send',
            method: 'post',
            data: data,
        })
    },
    mailDelete: (data) => {
        return service({
            url: '/admin/api/game/mail/delete',
            method: 'post',
            data: data,
        })
    },
    cdkeyQuery: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/game/cdkey/query',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    cdkeyAdd: (data) => {
        return service({
            url: '/admin/api/game/cdkey/add',
            method: 'post',
            data: data
        })
    },
    cdkeyGenerate: (data) => {
        return service({
            url: '/admin/api/game/cdkey/generate',
            method: 'post',
            data: data
        })
    },
    cdkeyDelete: (keys) => {
        return service({
            url: '/admin/api/game/cdkey/delete',
            method: 'post',
            data: {ids: keys}
        })
    },
    userQuery: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/game/user/query',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    userOperate: (data) => {
        return service({
            url: '/admin/api/game/user/operate',
            method: 'post',
            data: data
        })
    },
    playerQuery: (appId, playerId) => {
        return service({
            url: '/admin/api/game/player/query',
            method: 'get',
            params: {appId: appId, playerId: playerId}
        })
    },
    playerOperate: (data) => {
        return service({
            url: '/admin/api/game/player/operate',
            method: 'post',
            data: data
        })
    }
}

export default gameApi