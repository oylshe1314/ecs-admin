import {defineStore} from 'pinia'

const homeCard = {
    id: 1,
    name: 'HomeView',
    title: '首页',
    path: '/home',
    closable: false,
};

const tabsStore = defineStore({
    id: 'tabs',
    state: () => {
        const value = sessionStorage.getItem('tabs.state')
        if (value && value !== '') {
            const sto = JSON.parse(value)
            return {
                names: sto.names,
                cards: sto.cards,
            }
        } else {
            return {
                names: [homeCard.name],
                cards: [homeCard],
            }
        }
    },
    getters: {
        getNames() {
            return this.names
        },
        getCards() {
            return this.cards
        },
        index() {
            return (name) => {
                for (const i in this.cards) {
                    if (this.cards[i].name === name) {
                        return i
                    }
                }
                return -1
            }
        },
        size() {
            return this.cards.length
        }
    },
    actions: {
        add(tab) {
            this.names.push(tab.name)
            this.cards.push(tab)
        },
        remove(index) {
            this.names.splice(index, 1)
            this.cards.splice(index, 1)
        },
        clear() {
            sessionStorage.removeItem("tabs.state")
            this.$reset()
        }
    }
})

export default tabsStore
