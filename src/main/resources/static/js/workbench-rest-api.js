/**
 * チケットを更新する
 * @param ticket チケット
 */
function updateTicket(ticket) {
    return axios.post('/rest/ticket', ticket, {
        headers: {
            'X-XSRF-TOKEN': Cookies.get('XSRF-TOKEN')
        }
    });
}

/**
 * すべてのユーザーを取得する
 */
function fetchAllUser() {
    return axios.get('/rest/user/all');
}