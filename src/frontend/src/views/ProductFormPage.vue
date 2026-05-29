<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppHeader from '../components/AppHeader.vue'
import { createProduct, fetchProduct, updateProduct } from '../api/products'
import { fetchCategories } from '../api/categories'
import { getProductImageUrl } from '../utils/product'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const loading = ref(false)
const saving = ref(false)
const error = ref('')
const imageFile = ref(null)
const previewUrl = ref('')

const form = ref({
  name: '',
  price: '',
  description: '',
  categoryId: '',
})

const isEditMode = computed(() => route.name === 'product-edit')
const pageTitle = computed(() => (isEditMode.value ? 'Edit Product' : 'Add Product'))

async function loadCategories() {
  const { data } = await fetchCategories()
  categories.value = data
  if (!form.value.categoryId && data.length > 0) {
    form.value.categoryId = data[0].id
  }
}

async function loadProduct() {
  if (!isEditMode.value) {
    return
  }

  loading.value = true
  error.value = ''

  try {
    const { data } = await fetchProduct(Number(route.params.id))
    form.value = {
      name: data.name,
      price: data.price,
      description: data.description ?? '',
      categoryId: data.categoryId,
    }
    previewUrl.value = getProductImageUrl(data.imageUrl)
  } catch {
    error.value = 'Could not load product for editing.'
  } finally {
    loading.value = false
  }
}

function onImageChange(event) {
  const file = event.target.files?.[0]
  imageFile.value = null;
  if (file) {
    const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg'];
    const allowedExts = ['.jpg', '.jpeg', '.png'];
    const fileType = file.type;
    const fileName = file.name.toLowerCase();
    const validType = allowedTypes.includes(fileType);
    const validExt = allowedExts.some(ext => fileName.endsWith(ext));
    if (!validType || !validExt) {
      error.value = 'Only .jpg, .jpeg, and .png files are allowed.';
      previewUrl.value = '';
      return;
    }
    imageFile.value = file;
    previewUrl.value = URL.createObjectURL(file);
    error.value = '';
  }
}

async function submitForm() {
  saving.value = true
  error.value = ''

  const payload = {
    name: form.value.name.trim(),
    price: Number(form.value.price),
    description: form.value.description.trim(),
    categoryId: Number(form.value.categoryId),
  }

  try {
    if (isEditMode.value) {
      const { data } = await updateProduct(
        Number(route.params.id),
        payload,
        imageFile.value,
      )
      router.push(`/products/${data.id}`)
    } else {
      const { data } = await createProduct(payload, imageFile.value)
      router.push(`/products/${data.id}`)
    }
  } catch (requestError) {
    error.value =
      requestError.response?.data?.message ??
      'Could not save product. Please check the form and try again.'
  } finally {
    saving.value = false
  }
}

function cancel() {
  if (isEditMode.value) {
    router.push(`/products/${route.params.id}`)
    return
  }
  router.push('/')
}

onMounted(async () => {
  await loadCategories()
  await loadProduct()
  if (!previewUrl.value) {
    previewUrl.value = '/image.png'
  }
})
</script>

<template>
  <div class="page">
    <AppHeader />

    <div class="page-body">
      <h1>{{ pageTitle }}</h1>
      <p v-if="loading" class="status">Loading form...</p>

      <form v-else class="product-form" @submit.prevent="submitForm">
        <div class="preview">
          <img :src="previewUrl" alt="Product preview" />
        </div>

        <div class="fields">
          <label>
            Product Name
            <input v-model="form.name" type="text" required maxlength="255" />
          </label>

          <label>
            Price
            <input
              v-model="form.price"
              type="number"
              min="0.01"
              step="0.01"
              required
            />
          </label>

          <label>
            Category
            <select v-model="form.categoryId" required>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </label>

          <label>
            Description
            <textarea v-model="form.description" rows="5" maxlength="2000" />
          </label>

          <label>
            Product Image
            <input type="file" accept="image/*" @change="onImageChange" />
          </label>

          <p v-if="error" class="error">{{ error }}</p>

          <div class="actions">
            <button type="button" class="btn secondary" @click="cancel">Cancel</button>
            <button type="submit" class="btn primary" :disabled="saving">
              {{ saving ? 'Saving...' : isEditMode ? 'Update Product' : 'Add Product' }}
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  background: #fff;
}

.page-body {
  max-width: 920px;
  margin: 0 auto;
  padding: 2rem 1.5rem 3rem;
}

.page-body h1 {
  margin: 0 0 1.5rem;
  color: #111;
}

.product-form {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 2rem;
  border: 2px solid #111;
  border-radius: 0.75rem;
  padding: 1.5rem;
}

.preview img {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  border: 2px solid #111;
  border-radius: 0.75rem;
  background: #f5f5f5;
}

.fields {
  display: grid;
  gap: 1rem;
}

label {
  display: grid;
  gap: 0.4rem;
  font-weight: 600;
  color: #111;
}

input,
select,
textarea {
  width: 100%;
  box-sizing: border-box;
  padding: 0.65rem 0.75rem;
  border: 2px solid #111;
  border-radius: 0.5rem;
  font: inherit;
  background: #fff;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 0.5rem;
}

.btn {
  padding: 0.65rem 1rem;
  border: 2px solid #111;
  border-radius: 0.5rem;
  font: inherit;
  font-weight: 600;
  cursor: pointer;
}

.btn.primary {
  background: #111;
  color: #fff;
}

.btn.secondary {
  background: #fff;
  color: #111;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.status,
.error {
  margin: 0;
  padding: 0.75rem 1rem;
  border: 2px solid #111;
  border-radius: 0.5rem;
}

.error {
  color: #b71c1c;
}

@media (max-width: 800px) {
  .product-form {
    grid-template-columns: 1fr;
  }
}
</style>
