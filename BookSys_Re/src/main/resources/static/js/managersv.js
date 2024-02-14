
var aa = $('input[name=reservationjson]').val();
var j = JSON.parse(aa);

ReservationTableCreate();
function ReservationTableCreate(){
    var tc = new Array();
    var html = '';

    for(key in j) {
        console.log('key:' + key + ' / ' + 'value:' + j[key]);
        var j2 = JSON.parse(j[key]);

        html += '<tr id="tr_'+key+'">';
        html += '<td>'+key+'</td>';
        for(key2 in j2) {
            html += '<td>'+j2[key2]+'</td>';
        }
        html += '<td><input onclick="removeReservation('+key+')" type="button" value="취소"></td>'
        html += '</tr>';
    }

    $("#ReservationTbody").append(html);

}

function removeReservation(rno)
{
    let formData = new FormData();
    formData.append('rno',rno);
    formData.append('cmd',"remove");

    fetch('http://localhost:8080/reservation/managereservation',{
        method:'POST',
        body : formData
    });

    var element = document.getElementById('tr_'+rno);
    element.remove();
}

