let tg = window.Telegram.WebApp;

let p = document.createElement("p");

let data = tg.initData;
const obj = JSON.parse(data);
p.innerText = obj.type;
let tableDiv = document.getElementById("table_div");
tableDiv.appendChild(p);

// let table = document.createElement("table");
//
// let tHead = document.createElement("thead");
// let tBody = document.createElement("tbody");
//
// let tableDiv = document.getElementById("table_div");
//
// let data = tg.initDataUnsafe.user.id;
//
//
// tableDiv.appendChild(table);