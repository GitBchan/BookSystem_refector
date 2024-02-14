var cells = document.querySelectorAll("td"); // 테이블 셀 선택 메서드
var isChecked = false;
var CheckedCell;
var AddCell;
var rno = $('input[name=rno]').val();
var cid = $('input[name=cid]').val();
function isNull(v) {
    return (v === undefined || v === null) ? true : false;
}

cells.forEach(function (cell) {
    if (!cell.classList.contains("occupied")) {
        cell.addEventListener("click", function () {

            if(isChecked)
            {
                CheckedCell.classList.remove("selected");
            }

            this.classList.toggle("selected");
            CheckedCell = cell;
            isChecked = true;
            console.log("셀이 클릭되었습니다.");
            console.log(rno);
        });
    }

});



var addButton = document.querySelector("#addButton"); // 선택한 셀 예약 상태로 변경하는 메서드
addButton.addEventListener("click", function () {
    var selectedCells = document.querySelectorAll("td.selected");
    selectedCells.forEach(function (cell) {

        if(!isNull(AddCell))
            AddCell.classList.remove("reserved");

        cell.classList.remove("selected");
        cell.classList.add("reserved");

        let formData = new FormData();
        formData.append('id',cell.getAttribute("id"));
        formData.append('rno',rno);
        formData.append('cid',cid);
        formData.append('cmd',"add");

        console.log("해당 셀<"+cell.getAttribute("id")+">가 추가됩니다.");
        AddCell = cell;

        fetch('http://localhost:8080/reservation/selecttime',{
            method:'POST',
            body : formData
        });

    });
});

var removeButton = document.querySelector("#removeButton"); // 선택한 셀 예약 취소하는 메서드
removeButton.addEventListener("click", function () {
    var selectedCells = document.querySelectorAll("td.selected");
    selectedCells.forEach(function (cell) {
        cell.classList.remove("selected");
        cell.classList.remove("reserved");

        let formData = new FormData();
        formData.append('id',cell.getAttribute("id"));
        formData.append('rno',rno);
        formData.append('cid',cid);
        formData.append('cmd',"remove");

        console.log("해당 셀<"+cell.getAttribute("id")+">가 삭제됩니다.");

        fetch('http://localhost:8080/reservation/selecttime',{
            method:'POST',
            body : formData
        });
    });
});

document.querySelector("#endButton").addEventListener("click", function (e){
    e.stopPropagation()
    e.preventDefault()

    self.location = '/reservation/mainpage'
}, false)