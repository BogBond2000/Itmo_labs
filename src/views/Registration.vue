<template>
  <div class="container">
    <h1>Регистрация</h1>
    <input
        class="input"
        type="text"
        v-model="name"
        placeholder="Ваше имя"
    />
    <input
        class="input"
        type="password"
        v-model="password"
        placeholder="Пароль"
    />
    <button class="btn" @click="registerUser">Зарегистрироваться</button>
    <router-link class="link" to="/log">Уже есть аккаунт? Войти</router-link>

    <!-- Сообщение об ошибке или успехе -->
    <div v-if="responseMessage" class="response-message" :class="responseStatus">
      {{ responseMessage }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const name = ref('');
const password = ref('');
const responseMessage = ref('');
const responseStatus = ref('');
const router = useRouter();

const registerUser = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/users/register', {
      name: name.value,
      password: password.value
    });
    if (response.status === 201) {
      // Сохраняем имя пользователя и userId в localStorage
      localStorage.setItem('userName', name.value);
      localStorage.setItem('userId', response.data.userId); // Сохраняем userId

      // Устанавливаем сообщение об успехе
      responseMessage.value = 'Регистрация успешна!';
      responseStatus.value = 'success';

      // Переходим на главную страницу
      router.push('/home');
    } else {
      responseMessage.value = response.data.message;
      responseStatus.value = 'error';
    }
  } catch (error: any) {
    responseMessage.value = error.response?.data || 'Произошла ошибка при регистрации. Попробуйте еще раз.';
    responseStatus.value = 'error';
  }
};
</script>


<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: #fafafa;
}

h1 {
  font-size: 2rem;
  color: #333;
  margin-bottom: 20px;
  font-weight: 600;
}

.input {
  width: 90%;
  max-width: 400px;
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #dbdbdb;
  border-radius: 5px;
  font-size: 16px;
  background-color: #fff;
}

.input:focus {
  outline: none;
  border-color: #333;
}

.btn {
  background-color: #0095f6;
  color: white;
  border: none;
  margin-top: 10px;
  padding: 10px 15px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn:hover {
  background-color: #007cc4;
}

.link {
  margin-top: 15px;
  font-size: 14px;
  color: #0095f6;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

.response-message {
  margin-top: 15px;
  font-size: 14px;
  padding: 10px;
  border-radius: 5px;
  width: 90%;
  max-width: 400px;
  text-align: center;
}

.response-message.success {
  background-color: #e7f5e6;
  color: #28a745;
  border: 1px solid #28a745;
}

.response-message.error {
  background-color: #f8d7da;
  color: #dc3545;
  border: 1px solid #dc3545;
}
</style>
