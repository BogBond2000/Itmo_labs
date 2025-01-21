<template>
  <div class="container">
    <div class="card">
      <h2>Лабораторная работа №4</h2>
      <h3>Бондарь Богдан</h3>
      <h3>Вариант №4765</h3>
    </div>
    <iframe
        class="frame"
        src="https://free.timeanddate.com/clock/i939iwmt/n321/szw200/szh200/hoc009/hbw6/cf100/hgr0/hcc00/fas64/fdi86/mqcfff/mqs4/mql8/mqw4/mqd56/mhc90f/mhs3/mhl15/mhw4/mhd56/mnx0/mhx0/mnw3/mnt0/mmv0"
        frameborder="0"
        width="250"
        height="250"
    ></iframe>
    <router-link class="btn" to="/home">Вернуться на главную</router-link>
  </div>
</template>

<script>
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #fafafa;
  color: #444;
}

.card {
  text-align: center;
  background: #fff;
  border: 1px solid #dbdbdb;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  padding: 20px 30px;
  border-radius: 10px;
  margin-bottom: 20px;
}

h2 {
  font-size: 1.5rem;
  color: #333;
}

h3 {
  color: #666;
  margin-top: 10px;
  font-size: 1rem;
}

.frame {
  margin-top: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.btn {
  background-color: #0095f6;
  color: white;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  text-decoration: none;
  border-radius: 5px;
  margin-top: 15px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn:hover {
  background-color: #007cc4;
}
</style>