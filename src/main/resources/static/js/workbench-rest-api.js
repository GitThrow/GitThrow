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