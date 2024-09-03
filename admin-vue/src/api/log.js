import service from "@/util/request";

const logApi = {
    queryCreate: (pageNo, pageSize, params) => {
        return service({
            url: '/admin/api/log/query/create',
            method: 'get',
            params: {pageNo: pageNo, pageSize: pageSize, ...params}
        })
    },
    queryOnline: (date, params) => {
        return service({
            url: '/admin/api/log/query/online',
            method: 'get',
            params: {date: date, ...params}
        })
    }
}

export default logApi