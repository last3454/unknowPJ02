import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    outDir: path.resolve(__dirname, '../src/main/resources/static'),
    chunkSizeWarningLimit:1600
  },
  server: {
    host: true,
    proxy: {
      '/api': {
        target: 'http://localhost:9080',
        changeOrigin: true // cross origin 허용
      },
      '/upload': {
        target: 'http://localhost:9080',
        changeOrigin: true // cross origin 허용
      }
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  plugins: [vue()]
})
