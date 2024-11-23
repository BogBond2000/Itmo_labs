document.getElementById('plotButton').addEventListener('click', () => {
    const input = document.getElementById('functionInput').value;

    // Разделяем введённые функции по строкам
    const functions = input.split('\n').map(line => line.trim()).filter(line => line.startsWith('y ='));

    if (functions.length === 0) {
        alert('Введите хотя бы одну функцию в формате "y = ..."');
        return;
    }

    const traces = []; // Массив для всех графиков
    const xStr = getXValue(); // Получаем значение x для отправки на сервер
    const YInput = document.getElementById('yInput').value;
    if(YInput == null){
        alert("Выберете значение y")
    }
    const Y = parseFloat(YInput);
    if (xStr === null) {
        alert('Выберите значение x');
        return;
    }


    functions.forEach((func) => {
        // Убираем "y =" и оставляем только выражение
        const expression = func.replace('y =', '').trim();

        try {
            const xValues = [];
            const yValues = [];
            for (let x = -10; x <= 10; x += 0.1) {
                xValues.push(x);
                // Вычисление y, заменяя "x" текущим значением
                const y = eval(expression.replace(/x/g, `(${x})`));
                yValues.push(y);
            }

            // Добавляем график в массив
            traces.push({
                x: xValues,
                y: yValues,
                mode: 'lines',
                name: `y = ${expression}`, // Подпись графика
            });
        } catch (error) {
            alert(`Ошибка при разборе функции: ${func}. Проверьте синтаксис.`);
        }
    });

    // Отображаем все графики
    Plotly.newPlot('plot', traces, {
        title: 'Графики функций',
        xaxis: { title: 'x' },
        yaxis: { title: 'y' },
    });

    // Передаём массив функций и значение x на сервер
    sendFunctionsToServer(functions, xStr, Y);
});

// Функция для отправки функций на сервер
const sendFunctionsToServer = async (functions, xValue, yValue) => {
    try {
        const response = await fetch('/demo1_war_exploded/dop', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                functions: functions,  // Массив функций
                x: xValue,             // Значение x
                y: yValue
            }),
        });

        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }

        const result = await response.json();
        console.log('Ответ от сервера:', result);
    } catch (error) {
        console.error('Ошибка при отправке функций и x на сервер:', error);
    }
};

// Функция для получения значения x из выбранного чекбокса
function getXValue() {
    const checkboxes = document.querySelectorAll('input[name="xValue"]:checked');
    if (checkboxes.length > 0) {
        return parseFloat(checkboxes[0].value); // Парсим значение x из чекбокса
    }
    return null; // Возвращаем null, если ничего не выбрано
}
