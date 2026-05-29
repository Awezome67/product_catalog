import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/HomePage.vue'
import ProductDetailPage from '../views/ProductDetailPage.vue'
import ProductFormPage from '../views/ProductFormPage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomePage },
    { path: '/products/new', name: 'product-create', component: ProductFormPage },
    { path: '/products/:id/edit', name: 'product-edit', component: ProductFormPage },
    { path: '/products/:id', name: 'product-detail', component: ProductDetailPage },
  ],
})

export default router
