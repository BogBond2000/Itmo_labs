<template>
  <div class="container">
    <h1>Вход</h1>
    <input class="input" type="text" v-model="username" placeholder="Имя пользователя">
    <input class="input" type="password" v-model="password" placeholder="Пароль">
    <button class="btn" @click="loginUser">Войти</button>
    <router-link class="link" to="/reg">Нет аккаунта? Зарегистрироваться</router-link>
    <router-link class="link" to="/home">Вернуться на Главную</router-link>

    <div v-if="responseMessage" class="response-message" :class="responseStatus">
      {{ responseMessage }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const username = ref('');
const password = ref('');
const responseMessage = ref('');
const responseStatus = ref('');
const router = useRouter();

const loginUser = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/users/login', {
      name: username.value,
      password: password.value
    });

    if (response.status === 200) {
      localStorage.setItem('userId', response.data.split(":")[1].trim()); // Извлекаем userId
      localStorage.setItem('userName',username.value);
      router.push('/home'); // Перенаправление на главную страницу
    } else {
      responseMessage.value = response.data;
      responseStatus.value = 'error';
    }
  } catch (error) {
    responseMessage.value = error.response?.data || 'Ошибка авторизации. Попробуйте еще раз.';
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
}

.input {
  width: 90%;
  max-width: 400px;
  margin: 10px 0;
  padding: 12px;
  border: 1px solid #dbdbdb;
  border-radius: 5px;
  font-size: 16px;
}

.input:focus {
  outline: none;
  border-color: #333;
}

.btn {
  background-color: #0095f6;
  color: #fff;
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
</style>