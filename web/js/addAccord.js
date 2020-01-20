/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var oldAccord = null;
var oldURL = null;

function example(input) {
    console.log(input.files);
    var parent = $("#pdfList");
    for (let i = 0; i < input.files.length; i++) {
        listNewFile(parent, input.files[i]);
    }
    //input.files.forEach(file=>{
    //    listNewFile(parent,file);
    // });
}


function changeSwitch(input) {
    if (input.checked)
        document.getElementById('generalSession').disabled = false;
    else
        document.getElementById('generalSession').disabled = true;
}
//function openfile(file){
//    window.open(URL.createObjectURL(file));
//}

//function listNewFile(parent,file){
//    var tr = $("<tr/>");
//    tr.html(
//            "<td>" + file.name + "</td>"
//            +"<td>" +"<button type=\"button\" class=\"btn btn-success\" id=\""+file.name+"\" >Ver</button>" + "</td>"+
//            "<td>" +"<button type=\"button\" class=\"btn btn-danger\" disabled>Borrar</button>" + "</td>"
//            
//            );
//      parent.append(tr);
//  //  console.log("filename: "+file.name);
//    //console.log(document.getElementById(file.name));
//    document.getElementById(file.name).onclick=()=>{
//      openfile(file);  
//    };
//  
//}
function deletePdf(parent, pdf) {

    let newURL = [];
    for (let i = 0; i < oldAccord.URL.length; i++) {
        if (oldAccord.URL[i] === pdf)
            oldURL.push(oldAccord.URL[i]);
        else
            newURL.push(oldAccord.URL[i]);

    }
    oldAccord.URL = newURL;
    var row = parent.parentNode.parentNode;
    row.parentNode.removeChild(row);

}
function openPdf(pdf) {
    console.log(pdf);
    let _url = 'api/accord/getfile?filepath=' + pdf;
    console.log(_url);
    fetch(_url)
            .then(response => response.blob())
            .then(data => window.open(URL.createObjectURL(data)));
}
function list(parent, pdf) {
    var tr = $("<tr/>");
    if (sessionStorage.ROLE !== 'Concejo Municipal') {
        tr.html(
                "<td>" + pdf.substring(23, pdf.length) + "</td>"
                + "<td>" + "<button type=\"button\" class=\"btn btn-success\" onclick=\"javascript:openPdf('" + pdf + "')\">Ver</button>" + "</td>" +
                "<td>" + "<button type=\"button\" class=\"btn btn-danger\" onclick=\"javascript:deletePdf(this,'" + pdf + "')\" disabled>Borrar</button>" + "</td>"

                );
    } else
        tr.html(
                "<td>" + pdf.substring(23, pdf.length) + "</td>"
                + "<td>" + "<button type=\"button\" class=\"btn btn-success\" onclick=\"javascript:openPdf('" + pdf + "')\">Ver</button>" + "</td>" +
                "<td>" + "<button type=\"button\" class=\"btn btn-danger\" onclick=\"javascript:deletePdf(this,'" + pdf + "')\">Borrar</button>" + "</td>"

                );
    parent.append(tr);
}


//this fuction adds into an array the parameters of the url 
function getUrlVars() {
    var vars = {};
    var flag = false;
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
        flag = true;
    });
    if (!flag)
        return null;

    return vars;
}

function ready() {
    console.log(sessionStorage.ROLE);
    if (sessionStorage.ROLE === undefined)
        window.location.replace('/Kronos/login');


    setTypeOptions();
    setStateOptions();

    var number = getUrlVars();


    if (number !== null) {
        var accnumber = number.accnumber;
        if (accnumber !== undefined) {
            if (sessionStorage.ROLE !== undefined && sessionStorage.ROLE !== 'Concejo Municipal' && sessionStorage.ROLE !== 'Secretaria de Alcaldia')
                window.location.replace('/Kronos/authenticationError.jsp#popup');

            let _url = "api/accord/getaccord/" + accnumber;

            fetch(_url)
                    .then(res =>
                        res.json()
                    )
                    .then(accord => {
                        console.log(accord);
                        editAccord(accord);
                    })
                    .catch(error => {
                        console.log(error);
                    });

        }
    } else {
        if (sessionStorage.ROLE !== undefined && sessionStorage.ROLE !== 'Concejo Municipal')
            window.location.replace('/Kronos/authenticationError.jsp#popup');
    }
}
$(document).ready(ready);

//This function validates if a given date is weekend or holiday
function isValidDate(date) {
    let fecha = date;

    //array with all the posible holidays of the year excluding easter
    let holiday = [
        '01-01', // New year 
        '04-11', // Rivas battle
        '05-01', // Labor day
        '07-25', // Annexation of Guanacaste
        '08-02', // Our Lady of Los Ángeles
        '08-15', // Mother's day
        '09-15', // Independance's day
        '10-12', // Day of the Cultures
        '12-25'  // Christmas Day
    ];

    for (let i = 0; i < holiday.length; i++) {
        let aux = '' + fecha.getFullYear + '-' + holiday[i];
        if (date === aux)
            return false;
    }

    if (fecha.getMonth() === 2 || fecha.getMonth() === 3) {
        let URL = "https://www.googleapis.com/calendar/v3/calendars/" +
                "es.cr%23holiday%40group.v.calendar.google.com/events?key=AIzaSyAJuTl"
                + "-gxVHcY80RvGL8T9fzY8sgfwU8Xw";
        var result = true;
        $.getJSON(URL).then(data => {
            data.items.forEach(item => {
                //if the day is eastern   
                if (item.summary === 'Jueves Santo' || item.summary === 'Viernes Santo') {
                    if (date === item.start.date) {
                        result = false;
                        return false;
                    }

                }
            })

            return result;
        });

    }
    return true;
}


function changeComboType() {
    let combo = document.getElementById('comboTypes').value;
    let labelName = document.getElementById('labelName');
    let labelEmail = document.getElementById('labelEmail');
    let textName = document.getElementById('username');
    let textEmail = document.getElementById('email');
    if (combo !== 'A') {

        textName.value = '';
        textEmail.value = '';
        textName.type = 'text';
        textEmail.type = 'text';
        labelName.style.visibility = 'visible';
        labelEmail.style.visibility = 'visible';
    } else {
        textName.value = '';
        textEmail.value = '';
        textName.type = 'hidden';
        textEmail.type = 'hidden';
        labelName.style.visibility = 'hidden';
        labelEmail.style.visibility = 'hidden';
    }

}


function addAccord() {

//    if (!isValidDate(deadline.value)) {
//        alert("La Fecha de Vencimiento no es válida");
//        return;
//    }
    let deadline = document.getElementById('deadline');
    deadline.disabled = false;
    if (deadline.value === '') {
        alert("Por favor agregue una fecha de vencimiento");
        return;
    }
    let generalSession = document.getElementById('generalSession');
    let disabled = generalSession.disabled;
    generalSession.disabled = false;
    var generalSessionValue = generalSession.value;
    if (generalSession.value === '') {
        alert("Por favor agregue la fecha de Sesión");
        return;
    }
    let _url = '/Kronos/accord/addAccord';

    var form = document.getElementById('uploadForm');
    console.log(form);
    var _data = new FormData(form);
    console.log(_data);
    $.ajax({
        type: "POST",
        encType: "multipart/form-data",
        url: _url,
        cache: false,
        processData: false,
        contentType: false,
        data: _data,
        success: function (msg) {
            alert("ACUERDO AGREGADO EXITOSAMENTE");
        },
        error: function (response) {
            console.log(response);
            if (response.status === 503) {
                alert("OCURRIO UN ERROR");
            } else
                alert("OCURRIO UN ERROR");
        }
    }).then(() => {
        let value = document.getElementById('customSwitch1').checked;



        form.reset();

        if (!value) {
            document.getElementById('generalSession').value = generalSessionValue;
            document.getElementById('generalSession').disabled = true;
        }
        document.getElementById('customSwitch1').checked = value;
        document.getElementById('deadline').disabled = true;

    });


}

function editAccord(accord) {

    console.log(accord);
    oldAccord = accord;
    oldURL = [];
    let sessionDate = document.getElementById('generalSession');
    let office = document.getElementById('office');
    let days = document.getElementById('days');
    let comboDays = document.getElementById('comboDays');
    let deadline = document.getElementById('deadline');
    let daysButton = document.getElementById('daysButton');
    let comboTypes = document.getElementById('comboTypes');
    let username = document.getElementById('username');
    let email = document.getElementById('email');
    let comboStates = document.getElementById('comboStates');
    let observations = document.getElementById('observations');
    let labelStates = document.getElementById('labelState');
    let pdf = document.getElementById('accord');
    let divFile = document.getElementById('files');
    let table = document.getElementById('table');
    let cancelButton = document.getElementById('cancelButton');
    let title = document.getElementById('pageTitle');
    let okButton = document.getElementById('okButton');
    let divDeleteButton = document.getElementById('divDeleteButton');
    let divDeadline = document.getElementById('divDeadline');
    let deleteButton = document.getElementById('deleteButton');
    let pageTitle = document.getElementById('prinTitle');
    let divSwitch = document.getElementById('divSwitch');
    //let addPdfButton=document.getElementById('addPdfButton');
    pageTitle.innerHTML = "";
    pageTitle.innerHTML = 'Modificar Acuerdo';
    title.innerHTML = "";
    title.innerHTML = 'Modificar Acuerdo';
    sessionDate.value = accord.sessionDate;
    sessionDate.disabled = true;
    office.value = accord.accNumber.substring(14, accord.accNumber.length);
    office.disabled = true;
    if (sessionStorage.ROLE !== 'Concejo Municipal') {
        observations.value = accord.observations;
        observations.disabled = true;
        deadline.value = accord.deadline;
        deadline.disabled = true;
        comboStates.value = accord.state;
        comboStates.disabled = true;
        comboTypes.value = accord.type;
        comboTypes.disabled = true;
        comboStates.style.visibility = 'visible';
        labelStates.style.visibility = 'visible';
        daysButton.style.visibility = 'hidden';
        divFile.style.display = 'none';
        divDeadline.style.display = 'none';
        table.style.display = 'block';
        okButton.onclick = editButton;
        okButton.innerHTML = "";
        okButton.innerHTML = "Modificar Acuerdo";
        okButton.disabled = true;
        divDeleteButton.style.visibility = 'hidden';
        divSwitch.style.visibility = 'hidden';
        comboTypes.disabled = true;



    } else {
        observations.value = accord.observations;
        deadline.value = accord.deadline;
        deadline.disabled = false;
        comboStates.value = accord.state;
        comboTypes.value = accord.type;
        comboStates.style.visibility = 'visible';
        labelStates.style.visibility = 'visible';
        daysButton.style.visibility = 'hidden';
        divFile.style.display = 'none';
        divDeadline.style.display = 'none';
        table.style.display = 'block';
        okButton.onclick = editButton;
        okButton.innerHTML = "";
        okButton.innerHTML = "Modificar Acuerdo";
        deleteButton.onclick = deleteAccord;
        divDeleteButton.style.visibility = 'visible';
       // addPdfButton.style.visibility = 'visible';
        comboTypes.disabled = true;
        console.log(okButton);


    }

    var parent = $("#pdfList");
    parent.html("");
    accord.URL.forEach(item => {
        list(parent, item);
    });
}



//it adds an amount of days the the date
//even if it has to change the month or the year
function addDays(days, date) {

    var result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}


//this function gets the deadline date counting the weekends and the holidays
function getDeadline_NaturalDays(_date) {
    let days = parseInt(document.getElementById('days').value, 10);
    //adding the days to the date
    let date = addDays(days, _date);

    //if the resulting date is a weekend
    if (date.getDay() === 6 || date.getDay() === 0) {
        date = fixWeekend(date);
    }

    // if the resulting date is not a holiday
    //then return it
    //else just add one day to the resulting date 
    //until isnt a holiday or a weekend
    if (isValidDate(date)) {
        return date;
    } else {

        for (; ; ) {
            date = addDays(1, date);
            if (date.getDay() === 6 || date.getDay() === 0) {
                date = fixWeekend(date);
            }
            if (isValidDate(date))
                return date;
        }

    }

}

//this function gets the deadline without counting the weekends
// or the holidays
function getDeadline_BussinessDays(date) {
    let days = parseInt(document.getElementById('days').value, 10);
    //counting the current day
    let aux = 1;

    for (; ; ) {
        // if the counting is equal to the amount of days selected
        if (aux === days) {
            //if isnt a holiday or a weekend return the current date
            if (date.getDay() !== 0 && date.getDay() !== 6 && isValidDate(date)) {
                return date;
            } else {

                //if the resulting date is a weekend, fix it
                if (date.getDay() === 0 || date.getDay() === 6) {
                    date = fixWeekend(date);
                } else {
                    //add one day to the resulting date to avoid the holiday
                    date = addDays(1, date);
                }
                for (; ; ) {
                    //return the resulting date if isnt a weekend or a holiday
                    if (date.getDay() !== 0 && date.getDay() !== 6 && isValidDate(date))
                        return date;
                    else
                        date = addDays(1, date);
                    //else just keep adding one day to the resulting date until isnt a 
                    //holiday or a weekend
                }
            }
        }

        // it only counts when isnt weekend or holiday
        if (date.getDay() !== 0 && date.getDay() !== 6 && isValidDate(date))
            aux++;

        //adds one day to the date
        date = addDays(1, date);
    }


}


//this function adds 1 day to the date if the day is sunday 
//and 2 days if the day is saturday 
function fixWeekend(date) {
    if (date.getDay() === 6)
        return addDays(2, date);
    else
        return addDays(1, date);


}

function setTypeOptions() {
    let select = document.getElementById('comboTypes');
    let url = "api/type/getall";
    fetch(url)
            .then(response => response.json())
            .then(data => {
                data.forEach(item => {
                    var opt = document.createElement('option');
                    opt.value = item.id;
                    opt.innerHTML = item.description;
                    select.appendChild(opt);
                });
            }).catch(error => console.log(error));

}

function setStateOptions() {
    let select = document.getElementById('comboStates');
    let url = "api/state/getall";
    fetch(url)
            .then(response => response.json())
            .then(data => {
                data.forEach(item => {
                    var opt = document.createElement('option');
                    opt.value = item.id;
                    opt.innerHTML = item.description;
                    select.appendChild(opt);
                });
            }).catch(error => console.log(error));
}


function formatDate(d) {
    var month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
}
function setDeadline() {
    date = new Date();
    let output = document.getElementById('deadline');
    let select = document.getElementById('comboDays').value;
    alertDay();
    if (document.getElementById('days').value !== '') {
        let newDate = select === '1' ? getDeadline_BussinessDays(date) : getDeadline_NaturalDays(date);
        let format = formatDate(newDate);
        output.value = format;
    } else {
        alert("Por favor inserte el plazo");
    }
}

function alertDay() {
    if (document.getElementById('days').value !== '') {
        var days = parseInt(document.getElementById("days").value, 10);
        if (days > 60)
            if (!confirm("Usted ha seleccionado un plazo de: " + days + " días.\n ¿Desea Continuar?"))
                document.getElementById('days').value = '';
    }

}

function deletedPdf(oldArray, newArray) {

    return_array = [];
    oldArray.forEach(item => {
        let _it = newArray.find(string => string !== item);
        if (_it !== undefined)
            return_array.push(_it);

    });
    return return_array;
}


function editButton() {
    if (confirm("¿Seguro que desea modificar el acuerdo? ")){
    let newAccord = getNewAccord();
            compareAccords(oldAccord, newAccord).then(() => {
    alert("Acuerdo Actualizado Correctamente");
            oldAccord = null;
            oldURL = null;
            window.location.replace("/Kronos/listAccord")
            console.log("NO ERROR");
    }).catch(() => {
    console.log("ERRORRR");
            alert("Ha Ocurrido un error");
            oldAccord = null;
            oldURL = null;
            window.location.replace("/Kronos/listAccord")
    })

    }

}
function getNewAccord() {

    let sessionDate = document.getElementById('generalSession');
    let office = document.getElementById('office');
    let days = document.getElementById('days');
    let comboDays = document.getElementById('comboDays');
    let deadline = document.getElementById('deadline');
    let daysButtons = document.getElementById('daysButtons');
    let comboTypes = document.getElementById('comboTypes');
    let username = document.getElementById('username');
    let email = document.getElementById('email');
    let comboStates = document.getElementById('comboStates');
    let observations = document.getElementById('observations');
    let labelStates = document.getElementById('labelState');
    let pdf = document.getElementById('accord');
    let divFile = document.getElementById('files');
    let table = document.getElementById('table');
    let cancelButton = document.getElementById('cancelButton');
    var newAccord = {};
    newAccord.accNumber = oldAccord.accNumber;
    newAccord.deadline = deadline.value;
    newAccord.type = comboTypes.value;
    sessionDate.disabled = false;
    newAccord.sessionDate = sessionDate.value;
    newAccord.observations = observations.value;
    newAccord.state = comboStates.value;
    return newAccord;
}
function compareAccords(oldAccord, newAccord) {


    return new Promise((resolve, reject) => {

        if (oldAccord.sessionDate !== newAccord.sessionDate)
            updateSessionDate(newAccord,data=>resolve(data),err => reject(err));

        if (oldAccord.deadline !== newAccord.deadline)
            updateDeadline(newAccord,data=>resolve(data),err => reject(err));

//    if(oldAccord.notificationDate !== newAccord.notificationDate)
//        noError=updateNotDate(newAccord);
//    
        if (oldAccord.observations !== newAccord.observations)
            updateObservations(newAccord,data=>resolve(data) ,err => reject(err));

        if (oldAccord.type !== newAccord.type)
                updateType(newAccord,data=>resolve(data) ,err => reject(err));

        if (oldAccord.state !== parseInt(newAccord.state, 10))
                updateState(newAccord,data=>resolve(data),err => reject(err));

        if (oldURL.length > 0)
                updateDeletedPdf(newAccord,data=>resolve(data),oldURL,err => reject(err));

    });

}

const updateObservations = (newAccord,succ ,callback) => {
    let url = 'api/accord/updateObservations/' + newAccord.accNumber;

    $.ajax({
        type: "POST",
        async: false,
        contentType: " text/plain; charset=utf-8",
        data: newAccord.observations,
        url: url,
        success:succ,
        error: callback

    });
}
function updateSessionDate(newAccord,succ, callback) {
    let url = 'api/accord/updateSessionDate/' + newAccord.accNumber + '/' + newAccord.sessionDate;
    $.ajax({
        type: "POST",
        async: false,
        url: url,
        success:succ,
        error: callback

    });
}


function updateDeadline(newAccord,succ,callback) {
    let url = 'api/accord/updateDeadline/' + newAccord.accNumber + '/' + newAccord.deadline;
    $.ajax({
        type: "POST",
        async: false,
        url: url,
        success:succ,
        error: callback

    });
}

function updateType(newAccord, succ, callback) {
    let url = 'api/accord/updateType/' + newAccord.accNumber + '/' + newAccord.type;

    $.ajax({
        type: "POST",
        async: false,
        url: url,
        success:succ,
        error: callback

    });
}


function updateState(newAccord, succ,callback) {
    let url = 'api/accord/updateState/' + newAccord.accNumber + '/' + newAccord.state;

    $.ajax({
        type: "POST",
        url: url,
        success:succ,
        error: callback

    });

}

function updateDeletedPdf(newAccord, oldURL, succ,callback) {
    let url = 'api/accord/deletePDF/' + newAccord.accNumber;

    var noError = true;
    $.ajax({
        type: "POST",
        url: url,
        async: false,
        contentType: "application/json",
        data: JSON.stringify(oldURL),

        success:succ,
        error: callback

    });

}


function deleteAccord() {
    if (confirm("Esta seguro de que desea eliminar el acuerdo")) {
        let url = 'api/accord/deleteAccord/' + oldAccord.accNumber + '/' + sessionStorage.USER;

        $.ajax({
            type: "POST",
            url: url,
            success: function (msg) {
                console.log(msg);
                oldAccord = null;
                oldURL = null;
                alert("Acuerdo Eliminado Correctamente");
                window.location.replace("/Kronos/listAccord");
            },
            error: function (msg) {
                console.log(msg);
                alert("Ocurrio Un Error");

            }

        });
    }
}



function cancelAction() {
    window.location.replace(document.referrer);
}
/*
 * 
 * get the pdf and display it in a new browser tab
 * 
 *             function prueba(){
 let path='/home/jonathan/uploads/docu.pdf';
 let _url='api/accord/getfile?filepath='+path;
 console.log(_url);
 fetch(_url)
 .then(response => response.blob())
 .then(data => window.open(URL.createObjectURL(data)));
 }
 * 
 * 
 
 */


/* this function call the pop up and print date */

//function alertDay(){
//    var txt = 0;
//    var deadline = document.getElementsByID("deadline").value;
//    
//    if (deadline > 60){
//        if (confirm("Usted ha seleccionado un plazo de: " + deadline.values())) {
//            txt = "El plazo es de " + deadline + " días";
//        }        
//    }
//    else {
//        txt= "La fecha es " + deadline;
//        
//    }
//    document.getElementById("days").innerHTML = txt;
//}


/*this function confirm the funtion add Accord*/

