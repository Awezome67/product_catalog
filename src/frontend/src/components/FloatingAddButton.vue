<template>
  <router-link to="/products/new" class="fab" aria-label="Add new product">
    +
  </router-link>
</template>

<style scoped>
.fab {
  position: fixed;
  right: 1.5rem;
  bottom: 1.5rem;
  width: 3.5rem;
  height: 3.5rem;
  border-radius: 50%;
  border: 2px solid #111;
  background: #fff;
  color: #111;
  font-size: 2rem;
  line-height: 1;
  display: grid;
  place-items: center;
  text-decoration: none;
  box-shadow: 4px 4px 0 #111;
  z-index: 100;
}

.fab:hover {
  background: #111;
  color: #fff;
}
</style>
