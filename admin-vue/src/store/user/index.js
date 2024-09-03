import userStore from './store';

const user = userStore()

user.$subscribe((mutation, state) => {
    if (state.info == null) {
        sessionStorage.removeItem('user.info')
    } else {
        sessionStorage.setItem('user.info', JSON.stringify(state.info))
    }
})

export default user
