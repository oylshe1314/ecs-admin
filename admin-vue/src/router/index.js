import {createRouter, createWebHistory} from 'vue-router'

import store from '@/store'
import userStore from '@/store/user/store'
import tabsStore from '@/store/tabs/store'

const routes = [
    {
        path: "/",
        redirect: '/index'
    },
    {
        path: '/login',
        name: 'LoginPage',
        component: () => import('@/pages/LoginPage'),
        meta: {
            title: '登录',
        }
    },
    {
        path: "/index",
        name: 'IndexPage',
        component: () => import('@/pages/IndexPage'),
        children: [
            {
                path: '/home',
                name: 'HomeView',
                component: () => import('@/pages/views/HomeView'),
                meta: {
                    tabView: true,
                    title: "首页"
                }
            },
            {
                path: '/404',
                name: 'NotFound',
                component: () => import('@/pages/views/NotFound'),
                meta: {
                    tabView: true,
                    title: "404",
                    closable: true,
                }
            },
            {
                path: '/menu/index',
                name: 'MenuIndex',
                component: () => import('@/pages/views/menu/MenusIndex'),
                meta: {
                    tabView: true,
                    title: "菜单列表",
                    closable: true,
                }
            },
            {
                path: '/admin/role/index',
                name: 'RoleIndex',
                component: () => import('@/pages/views/admin/role/RoleIndex'),
                meta: {
                    tabView: true,
                    title: "角色列表",
                    closable: true,
                }
            },
            {
                path: '/admin/index',
                name: 'AdminIndex',
                component: () => import('@/pages/views/admin/AdminIndex'),
                meta: {
                    tabView: true,
                    title: "管理员列表",
                    closable: true,
                }
            },
            {
                path: '/setting/detail',
                name: 'DetailView',
                component: () => import('@/pages/views/setting/DetailView'),
                meta: {
                    tabView: true,
                    title: "用户详情",
                    closable: true,
                }
            },
            {
                path: '/setting/change/password',
                name: 'ChangePassword',
                component: () => import('@/pages/views/setting/ChangePassword'),
                meta: {
                    tabView: true,
                    title: "修改密码",
                    closable: true,
                }
            },
            {
                path: '/log/create/index',
                name: 'CreateIndex',
                component: () => import('@/pages/views/log/CreateIndex'),
                meta: {
                    tabView: true,
                    title: "注册日志",
                    closable: true,
                }
            },
            {
                path: '/log/online/index',
                name: 'OnlineIndex',
                component: () => import('@/pages/views/log/OnlineIndex'),
                meta: {
                    tabView: true,
                    title: "实时在线",
                    closable: true,
                }
            },
            {
                path: '/server/type/index',
                name: 'TypeIndex',
                component: () => import('@/pages/views/server/type/TypeIndex'),
                meta: {
                    tabView: true,
                    title: "服务类型",
                    closable: true,
                }
            },
            {
                path: '/server/host/index',
                name: 'HostIndex',
                component: () => import('@/pages/views/server/host/HostIndex'),
                meta: {
                    tabView: true,
                    title: "主机列表",
                    closable: true,
                }
            },
            {
                path: '/server/config/index',
                name: 'ConfigIndex',
                component: () => import('@/pages/views/server/config/ConfigIndex'),
                meta: {
                    tabView: true,
                    title: "配置列表",
                    closable: true,
                }
            },
            {
                path: '/server/service/index',
                name: 'ServiceIndex',
                component: () => import('@/pages/views/server/service/ServiceIndex'),
                meta: {
                    tabView: true,
                    title: "服务列表",
                    closable: true,
                }
            },
            {
                path: '/server/gate/index',
                name: 'GateIndex',
                component: () => import('@/pages/views/server/gate/GateIndex'),
                meta: {
                    tabView: true,
                    title: "网关管理",
                    closable: true,
                }
            },
            {
                path: '/game/mail/index',
                name: 'MailIndex',
                component: () => import('@/pages/views/game/mail/MailIndex'),
                meta: {
                    tabView: true,
                    title: "邮件管理",
                    closable: true,
                }
            },
            {
                path: '/game/cdkey/index',
                name: 'CdkeyIndex',
                component: () => import('@/pages/views/game/cdkey/CdkeyIndex'),
                meta: {
                    tabView: true,
                    title: "兑换码管理",
                    closable: true,
                }
            },
            {
                path: '/game/user/index',
                name: 'UserIndex',
                component: () => import('@/pages/views/game/user/UserIndex'),
                meta: {
                    tabView: true,
                    title: "账号管理",
                    closable: true,
                }
            },
            {
                path: '/game/player/index',
                name: 'PlayerIndex',
                component: () => import('@/pages/views/game/player/PlayerIndex'),
                meta: {
                    tabView: true,
                    title: "玩家管理",
                    closable: true,
                }
            }
        ]
    },
    {
        path: '/:pathMatch(.*)',
        redirect: '/404'
    },
]

const user = userStore(store)
const tabs = tabsStore(store)

const router = createRouter({
    routes: routes,
    history: createWebHistory(process.env.BASE_URL),
})

router.beforeEach((to, from, next) => {
    if (to.meta && to.meta.title) {
        document.title = to.meta.title
    }
    if (to.name === 'LoginPage') {
        next()
        return;
    } else {
        if (!user.getInfo) {
            next({name: 'LoginPage'})
            return;
        }
    }

    if (to.meta.tabView && to.name !== 'HomeView') {
        if (tabs.index(to.name) < 0) {
            tabs.add({
                id: tabs.size + 1,
                name: to.name,
                title: to.meta.title,
                path: to.path,
                closable: to.meta.closable,
            })
        }
    }

    next()
})

export default router
