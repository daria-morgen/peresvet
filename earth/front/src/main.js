import { createApp } from 'vue'
import App from './App.vue'

import axios from 'axios'
import VueAxios from 'vue-axios'
// import { userSetter } from 'core-js/fn/symbol'


createApp(App).use(VueAxios,axios).mount('#app')
