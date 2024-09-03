export function stringToday() {
    const now = new Date();
    return now.getFullYear() + '-' + (now.getMonth() > 8 ? (now.getMonth() + 1) : '0' + (now.getMonth() + 1)) + '-' + (now.getDate() > 9 ? now.getDate() : '0' + now.getDate())
}