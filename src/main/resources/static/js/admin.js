
//admin bill button complete
const btnCompletes = document.querySelectorAll('.btn_complete')
console.log(btnCompletes.length)

btnCompletes.forEach((btnComplete) => {
    btnComplete.onclick = () => {
        btnComplete.classList.add('complete')
        btnComplete.innerText = 'Đã hoàn thành'
    }
})