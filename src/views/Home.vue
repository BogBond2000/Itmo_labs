<template>
  <div class="home">
    <h1>Добро пожаловать {{username}}!</h1>
    <router-link class="btn" to="/log">Войти в профиль</router-link>
    <router-link class="btn-secondary" to="/reg">Зарегистрироваться</router-link>
    <router-link class="btn-secondary" to="/">Вернуться к часам</router-link>
    <button class="btn-logout" @click="logout">Выйти</button>
  </div>
  <div>
    <form @submit.prevent="drawGraph">
      <div>
        <label for="xInput">Введите X (-3 до 5):</label>
        <input v-model.number="x" id="xInput" type="number" />
        <span v-if="errors.xError" class="error">{{ errors.xError }}</span>
      </div>
      <div>
        <label for="yInput">Введите Y (-3 до 5):</label>
        <input v-model.number="y" id="yInput" type="number" />
        <span v-if="errors.yError" class="error">{{ errors.yError }}</span>
      </div>
      <div>
        <label for="rSelect">Выберите R:</label>
        <select v-model.number="r" id="rSelect">
          <option disabled value="">Выберите R</option>
          <option v-for="value in rOptions" :key="value" :value="value">R = {{ value }}</option>
        </select>
        <span v-if="errors.rError" class="error">{{ errors.rError }}</span>
      </div>
      <button type="submit">Нарисовать</button>
    </form>
    <div class="canv">
      <canvas ref="graphCanvas" width="500" height="500"></canvas>
    </div>
  </div>
  <div>
    <table class="check-table">
      <thead>
      <tr>
        <th>X</th>
        <th>Y</th>
        <th>R</th>
        <th>Попал?</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(point, index) in checkPoints" :key="index">
        <td>{{ point.x }}</td>
        <td>{{ point.y }}</td>
        <td>{{ point.r }}</td>
        <td>{{ point.inArea ? 'Да' : 'Нет' }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: localStorage.getItem('userName'),
      checkPoints: [], // Массив для хранения точек пользователя
      x: null, // Значение X
      y: null, // Значение Y
      r: null, // Значение R
      isInArea: null,
      rOptions: [1, 2, 3, 4, 5], // Варианты R
      errors: {
        xError: null,
        yError: null,
        rError: null
      }
    };
  },
  mounted() {
    // Если пользователь авторизован, получаем его точки
    if (this.username) {
      this.fetchUserPoints();
    }
  },
  methods: {
    // Логика выхода из системы
    logout() {
      localStorage.removeItem('userId'); // Удаляем идентификатор пользователя
      localStorage.removeItem('userName'); // Удаляем имя пользователя
      this.username = null; // Обновляем локальное состояние
      alert('Вы успешно вышли из системы.');
      this.$router.push('/log'); // Перенаправляем на страницу входа
    },

    // Валидация данных
    validateNumberWithLimit(value, min, max) {
      return !isNaN(value) && value >= min && value <= max;
    },

    // Очистка ошибок
    clearErrors() {
      this.errors = { xError: null, yError: null, rError: null };
    },

    async fetchUserPoints() {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        alert('Вы должны войти в систему, чтобы загрузить ваши точки.');
        this.$router.push('/log');
        return;
      }

      try {
        // Запрос на получение всех точек пользователя
        const response = await axios.get(`http://localhost:8080/api/points/user/${userId}`);
        if (response.data) {
          this.checkPoints = response.data;// Заполняем массив точками
          console.log(response.data)
        }

      } catch (error) {
        console.error('Ошибка при загрузке точек:', error);
      }
    },

    // Отправка новой точки на сервер
    async drawGraph() {
      this.clearErrors();

      // Проверка авторизации
      const userId = localStorage.getItem('userId');
      if (!userId) {
        alert('Вы должны войти в систему, чтобы проверить точку.');
        this.$router.push('/log'); // Перенаправляем на страницу входа
        return;
      }

      // Валидация введенных данных
      let hasError = false;
      if (!this.r) {
        this.errors.rError = "Выберите значение R";
        hasError = true;
      }
      if (!this.validateNumberWithLimit(this.x, -3, 5)) {
        this.errors.xError = "Введите корректное значение X (-3 до 5)";
        hasError = true;
      }
      if (!this.validateNumberWithLimit(this.y, -3, 5)) {
        this.errors.yError = "Введите корректное значение Y (-3 до 5)";
        hasError = true;
      }
      if (hasError) return;

      // Формируем данные для отправки
      const pointData = {
        x: this.x,
        y: this.y,
        r: this.r,
        userId: +userId // Добавляем идентификатор пользователя
      };

      // Отправляем данные на сервер
      try {
        const response = await axios.post("http://localhost:8080/api/points/addPoints", pointData, {
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (response.status === 201 && response.data) {
          // Добавляем полученную точку в локальный массив checkPoints
          const newPoint = {
            x: this.x,
            y: this.y,
            r: this.r,
            inArea: response.data.inArea // Добавляем информацию о попадании в область
          };
          console.log(response.data)
          // Добавляем новую точку в массив
          this.checkPoints.push(newPoint);

          // Отрисовываем график только с новой точкой
          this.renderGraph(newPoint);
        } else {
          alert('Ошибка добавления точки');
        }
      } catch (error) {
        console.error("Ошибка запроса:", error);
      }
    },

    // Функция для отрисовки графика
    renderGraph(newPoint) {
      const canvas = this.$refs.graphCanvas;
      const ctx = canvas.getContext("2d");
      const centerX = canvas.width / 2;
      const centerY = canvas.height / 2;
      const scale = 30;

      // Очистка холста перед отрисовкой
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      // Рисуем оси
      ctx.beginPath();
      ctx.moveTo(0, centerY);
      ctx.lineTo(canvas.width, centerY);
      ctx.moveTo(centerX, 0);
      ctx.lineTo(centerX, canvas.height);
      ctx.strokeStyle = "black";
      ctx.stroke();

      // Прорисовка области (пример)
      ctx.fillStyle = "blue";
      ctx.beginPath();
      ctx.arc(centerX, centerY, this.r * scale / 2, Math.PI / 2, Math.PI);
      ctx.lineTo(centerX, centerY);
      ctx.closePath();
      ctx.fill();

      // Дополнительная геометрия (прямоугольник)
      ctx.fillRect(centerX, centerY, this.r * scale, this.r * scale / 2);

      // Прямоугольный треугольник
      ctx.beginPath();
      ctx.moveTo(centerX - this.r * scale, centerY);
      ctx.lineTo(centerX, centerY);
      ctx.lineTo(centerX, centerY - this.r * scale);
      ctx.closePath();
      ctx.fill();

      // Прорисовка только новой точки
      const pointX = centerX + newPoint.x * scale;
      const pointY = centerY - newPoint.y * scale;

      ctx.beginPath();
      ctx.arc(pointX, pointY, 5, 0, 2 * Math.PI);
      ctx.fillStyle = newPoint.isInArea ? "green" : "red";
      ctx.fill();
    }
  }
};
</script>


<style scoped>

.home {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
  text-align: center;
  padding: 20px;
  margin-bottom: 30px;
}

h1 {
  font-size: 2rem;
  color: #333;
  margin-bottom: 30px;
}

/* Кнопки */
.btn {
  background-color: #0095f6;
  color: white;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  text-decoration: none;
  border-radius: 5px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn:hover {
  background-color: #007cc4;
}

.btn-secondary {
  background-color: #fafafa;
  color: #0095f6;
  border: 1px solid #dbdbdb;
  padding: 10px 15px;
  margin: 10px;
  font-size: 16px;
  text-decoration: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn-secondary:hover {
  background-color: #f1f1f1;
}

/* Таблица */
.check-table {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  border-collapse: collapse;
  font-family: Arial, sans-serif;
  font-size: 14px;
  background: #fff;
  color: #333;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

thead {
  background-color: #f9f9f9;
}

th {
  text-align: left;
  padding: 10px 15px;
  border-bottom: 2px solid #ddd;
  color: #555;
  font-size: 14px;
  font-weight: 600;
}

tbody tr {
  border-bottom: 1px solid #ddd;
}

tbody tr:nth-child(odd) {
  background-color: #f9f9f9;
}

td {
  padding: 10px 15px;
  text-align: left;
}

tbody tr:hover {
  background-color: #f1f1f1;
}
/* Общий контейнер для формы и графики */


/* Заголовки и текст формы */
form label {
  display: block;
  font-size: 1rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

/* Поля ввода */
form input,
form select {
  width: 100%;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 15px;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

/* Hover/Focus эффекты для полей ввода */
form input:focus,
form select:focus {
  border-color: #0095f6;
  box-shadow: 0 0 5px rgba(0, 149, 246, 0.5);
  outline: none;
}

/* Кнопка отправки */
form button {
  background-color: #0095f6;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 1rem;
  font-weight: bold;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 100%;
}

form button:hover {
  background-color: #007cc4;
}

/* Ошибки */
.error {
  color: red;
  font-size: 0.875rem;
  margin-top: -10px;
  margin-bottom: 15px;
}

/* Для Canvas */
.canv{
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 10px;
  margin-top: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex; /* Включаем Flexbox */
  justify-content: center; /* Центрируем по горизонтали */
  align-items: center; /* Центрируем по вертикали */
}

.btn-logout {
  background-color: #ff4d4f;
  color: white;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
  transition: all 0.3s ease;
}

.btn-logout:hover {
  background-color: #d9363e;
}

</style>