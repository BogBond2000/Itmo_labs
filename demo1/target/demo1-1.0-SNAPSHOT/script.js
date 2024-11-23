function getXValue() {
    const checkboxes = document.querySelectorAll('input[name="xValue"]:checked');
    if (checkboxes.length > 0) {
        return parseFloat(checkboxes[0].value);
    }
    return null;
}

function getRValue() {
    const checkboxes = document.querySelectorAll('input[name="rValue"]:checked');
    if (checkboxes.length > 0) {
        return parseFloat(checkboxes[0].value);
    }
    return null;
}

function clearErrors() {
    document.getElementById('xError').textContent = '';
    document.getElementById('yError').textContent = '';
    document.getElementById('rError').textContent = '';
}

function showError(elementId, message) {
    document.getElementById(elementId).textContent = message;
}

function validateNumberWithLimit(value, min, max, decimals) {
    const regex = new RegExp(`^-?\\d+(\\.\\d{0,${decimals}})?$`);
    const numValue = parseFloat(value);
    return !isNaN(numValue) && regex.test(value) && numValue >= min && numValue <= max;
}

function savePoints(x, y, r) {
    const points = JSON.parse(localStorage.getItem('points')) || [];
    points.push({ x, y, r });
    localStorage.setItem('points', JSON.stringify(points));
}

function loadPoints(ctx, scale, centerX, centerY) {
    const points = JSON.parse(localStorage.getItem('points')) || [];
    points.forEach(point => {
        const pointX = centerX + point.x * scale;
        const pointY = centerY - point.y * scale;

        ctx.beginPath();
        ctx.arc(pointX, pointY, 5, 0, Math.PI * 2);
        ctx.fillStyle = 'red';
        ctx.fill();
    });
}

function drawGraph() {
    clearErrors();

    const canvas = document.getElementById('graphCanvas');
    const ctx = canvas.getContext('2d');
    const R = getRValue();
    const X = getXValue();
    const YInput = document.getElementById('yInput').value;
    const Y = parseFloat(YInput);
    let hasError = false;

    if (isNaN(R)) {
        showError('rError', "Выберете значение R");
        hasError = true;
    }
    if (isNaN(X)) {
        showError('xError', 'Выберите значение X');
        hasError = true;
    }
    if (!validateNumberWithLimit(Y, -3, 5, 2)) {
        showError('yError', 'Введите корректное значение Y');
        hasError = true;
    }
    if (hasError) return;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 30;

    ctx.beginPath();
    ctx.moveTo(0, centerY);
    ctx.lineTo(canvas.width, centerY);
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX, canvas.height);
    ctx.strokeStyle = 'black';
    ctx.stroke();

    ctx.fillStyle = 'blue';
    ctx.beginPath();
    ctx.arc(centerX, centerY, R * scale, Math.PI / 2, Math.PI);
    ctx.lineTo(centerX, centerY);
    ctx.closePath();
    ctx.fill();

    ctx.fillRect(centerX, centerY, -R * scale, -R * scale / 2);

    ctx.beginPath();
    ctx.moveTo(centerX + R * scale / 2, centerY);
    ctx.lineTo(centerX, centerY);
    ctx.lineTo(centerX, centerY + R * scale / 2);
    ctx.closePath();
    ctx.fill();

    const pointX = centerX + X * scale;
    const pointY = centerY - Y * scale;
    ctx.beginPath();
    ctx.arc(pointX, pointY, 5, 0, 2 * Math.PI);
    ctx.fillStyle = 'red';
    ctx.fill();

    savePoints(X,Y,R);

    // Загружаем точки из localStorage и рисуем их
    loadPoints(ctx, scale, centerX, centerY);

    sendHtml(X,Y,R)

}

document.getElementById('graphCanvas').addEventListener('click', event => {
    const canvas = event.target;
    const R = getRValue();
    if (isNaN(R)) {
        alert('Установите значение радиуса R');
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 30;

    const x = (event.clientX - rect.left - centerX) / scale;
    const y = (centerY - (event.clientY - rect.top)) / scale;
});

function clearSavedPoints() {
    localStorage.removeItem('points');
}


function sendHtml(x, y, r) {
    console.log(`Отправка данных: X=${x}, Y=${y}, R=${r}`);

    $.ajax({
        type: "POST",
        url: "/demo1_war_exploded/controller",
        contentType: "application/json",
        data: JSON.stringify({ x: x, y: y, r: r }),
        success: function(response) {
            console.log("Данные успешно отправлены:", response);
        },
        error: function(xhr, status, error) {
            console.error("Ошибка при отправке данных:", xhr.responseText, status, error);
            alert(`Ошибка! ${error}\nСтатус: ${status}\nОтвет сервера: ${xhr.responseText}`);
        }
    });
}




function setupCheckbox() {
    const checkboxesX = document.querySelectorAll('input[name="xValue"]');
    const checkboxesR = document.querySelectorAll('input[name="rValue"]');

    checkboxesX.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            checkboxesX.forEach(otherCheckbox => {
                if (otherCheckbox !== checkbox) {
                    otherCheckbox.checked = false; // Снимаем отметку с других чекбоксов
                }
            });
        });
    });

    checkboxesR.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            checkboxesR.forEach(otherCheckbox => {
                if (otherCheckbox !== checkbox) {
                    otherCheckbox.checked = false; // Снимаем отметку с других чекбоксов
                }
            });
            clearSavedPoints(); // Очистка сохраненных точек
        });
    });
}


window.onload = function() {
    setupCheckbox();
    drawGraph();
}
