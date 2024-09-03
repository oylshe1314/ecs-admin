import tabsStore from "./store";

const tabs = tabsStore()

tabs.$subscribe((mutation, state) => {
    sessionStorage.setItem("tabs.state", JSON.stringify(state))
})

export default tabs
