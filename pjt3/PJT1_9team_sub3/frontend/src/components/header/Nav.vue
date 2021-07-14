alert(response.data.user.user_email);
<template>
  <div id="wrap" @keydown.esc="toastEscListener">
    <div id="nav-container-pc" class="navigation">
      <div id="nav-logo" @click="clickLogo" class="b-title">iN.SSAFY</div>
      <div id="nav-btn-container">
        <button id="search-btn" class="icon-btn" @click="clickNBtn1" v-if="getToken != null">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
            <path
              d="M23.809 21.646l-6.205-6.205c1.167-1.605 1.857-3.579 1.857-5.711 0-5.365-4.365-9.73-9.731-9.73-5.365 0-9.73 4.365-9.73 9.73 0 5.366 4.365 9.73 9.73 9.73 2.034 0 3.923-.627 5.487-1.698l6.238 6.238 2.354-2.354zm-20.955-11.916c0-3.792 3.085-6.877 6.877-6.877s6.877 3.085 6.877 6.877-3.085 6.877-6.877 6.877c-3.793 0-6.877-3.085-6.877-6.877z"
            />
          </svg>
        </button>
        <button class="icon-btn" @click="clickNBtn2" v-if="getToken != null">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
            <path
              d="M19.619 21.671c-5.038 1.227-8.711-1.861-8.711-5.167 0-3.175 3.11-5.467 6.546-5.467 3.457 0 6.546 2.309 6.546 5.467 0 1.12-.403 2.22-1.117 3.073-.029 1 .558 2.435 1.088 3.479-1.419-.257-3.438-.824-4.352-1.385zm-10.711-5.167c0-4.117 3.834-7.467 8.546-7.467.886 0 1.74.119 2.544.338-.021-4.834-4.761-8.319-9.998-8.319-5.281 0-10 3.527-10 8.352 0 1.71.615 3.391 1.705 4.695.047 1.527-.851 3.718-1.661 5.313 2.168-.391 5.252-1.258 6.649-2.115.803.196 1.576.304 2.328.363-.067-.379-.113-.765-.113-1.16z"
            />
          </svg>
        </button>
        <Profile id="icon-profile" @clickProfile="clickNBtn3" v-if="getToken != null" />
        <!-- <button class="icon-btn" @click="clickNBtn3" v-if="getToken != null">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
            <path
              d="M19 7.001c0 3.865-3.134 7-7 7s-7-3.135-7-7c0-3.867 3.134-7.001 7-7.001s7 3.134 7 7.001zm-1.598 7.18c-1.506 1.137-3.374 1.82-5.402 1.82-2.03 0-3.899-.685-5.407-1.822-4.072 1.793-6.593 7.376-6.593 9.821h24c0-2.423-2.6-8.006-6.598-9.819z"
            />
          </svg>
        </button> -->
        <button
          id="login-btn"
          class="b-title"
          :class="{ hidden: isLoginRoute }"
          @click="clickLoginBtn"
          v-if="getToken == null"
        >
          Login
        </button>
      </div>
    </div>
    <div id="nav-container-mobile" class="navigation">
      <div id="search" class="m-btn" @click="clickSearch" v-if="getToken != null">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
          <path
            d="M23.809 21.646l-6.205-6.205c1.167-1.605 1.857-3.579 1.857-5.711 0-5.365-4.365-9.73-9.731-9.73-5.365 0-9.73 4.365-9.73 9.73 0 5.366 4.365 9.73 9.73 9.73 2.034 0 3.923-.627 5.487-1.698l6.238 6.238 2.354-2.354zm-20.955-11.916c0-3.792 3.085-6.877 6.877-6.877s6.877 3.085 6.877 6.877-3.085 6.877-6.877 6.877c-3.793 0-6.877-3.085-6.877-6.877z"
          />
        </svg>
      </div>
      <div id="nav-logo-m" @click="clickLogo" class="b-title">
        iN.SSAFY
      </div>
      <Profile id="icon-profile" class="m-btn" @clickProfile="clickHanberger" v-if="getToken != null" />
      <button id="login-btn2" class="m-btn" @click="clickLoginBtn" v-if="getToken == null">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
          <path
            d="M16.625 8.292c0 .506-.41.917-.917.917s-.916-.411-.916-.917.409-.917.916-.917.917.411.917.917zm7.375 3.708c0 6.627-5.373 12-12 12s-12-5.373-12-12 5.373-12 12-12 12 5.373 12 12zm-11.293 1.946c-1.142-.436-2.065-1.312-2.561-2.423l-3.146 3.185v2.292h3v-1h1v-1h.672l1.035-1.054zm5.293-4.279c0-2.025-1.642-3.667-3.667-3.667-2.024 0-3.666 1.642-3.666 3.667s1.642 3.667 3.666 3.667c2.025-.001 3.667-1.643 3.667-3.667z"
          />
        </svg>
      </button>
    </div>
    <div id="toast-container">
      <Toast id="toast" :class="{ toast_hide: !getToastActive }" />
      <div id="toast-bg" :class="{ toast_hide: !getToastActive }" @click="clickOutsideTheToast" />
    </div>
    <!-- <div class="the-blur"></div> -->
  </div>
</template>

<script>
import Toast from './Toast';
import Profile from '@/components/etc/Profile';
import { mapGetters, mapState } from 'vuex';
import routes from '@/routes';

export default {
  name: 'Nav',
  components: {
    Toast,
    Profile,
  },
  data() {
    return {};
  },
  computed: {
    ...mapGetters(['getUser', 'getToken', 'getToastActive']),
    ...mapState('search', ['active']),
    isLoginRoute: function() {
      return this.$route.name == 'Login';
    },
  },
  mounted() {},
  methods: {
    clickLogo: function() {
      this.$router.push({ name: 'Main' });
    },
    clickNBtn1: function() {
      const epicenter = this.$router.currentRoute.path;
      let active = 'allBoard';
      if (this.active === 'allBoard' || this.active === 'allPost') {
        active = this.active;
      }
      this.$store.commit('search/SET_SEARCH_ACTIVE', { active: active, epicenter: epicenter });
      this.$store.commit('setToastTogle');
      this.$store.commit('setToastType', 'search');
    },
    clickNBtn2: function() {
      this.$router.push({ name: 'ChatPage' });
    },
    clickNBtn3: function() {
      this.$store.commit('setToastTogle');
      this.$store.commit('setToastType', 'myinfo');
    },
    //moblie
    clickHanberger: function() {
      this.clickNBtn3();
    },
    clickSearch: function() {
      this.clickNBtn1();
    },
    clickLoginBtn: function() {
      this.$router.push({ name: 'Login' });
    },
    clickOutsideTheToast: function() {
      this.$store.commit('setToastClose');
    },

    toastEscListener() {
      this.$store.commit('setToastClose');
    },
  },
};
</script>

<style scoped>
/* 배경 흐림 효과 */
/* .the-blur {

} */

/* 로그인 페이지 일 때 로그인 버튼 숨기기 위해 */
#toast {
  position: fixed;
  z-index: 20;
  top: 0;
  background-color: var(--basic-color-bg);
  padding: 20px 0;
  transition: visibility 0.6s, opacity 0.6s, transform 0.6s ease;
}
#toast-bg {
  position: fixed;
  z-index: 10;
  top: 0;
  left: 0;
  width: 100%;
  height: 120%;
  background-color: #00000055;
  transition: visibility 0.6s, opacity 0.6s, transform 0.6s ease;
}
.hidden {
  display: none;
}

svg {
  fill: #000;
  width: 24px;
  height: 24px;
}
.navigation {
  top: 0;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  height: 50px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  background-color: #89defe;
}
#nav-container-pc {
  display: flex;
}
@media (max-width: 426px) {
  #nav-container-pc {
    display: none;
  }
}

#nav-logo {
  width: 160px;
  height: 40px;
  margin: 5px 10px;
  font-size: 30px;
  line-height: 38px;
  background-color: #21b0f5;
  cursor: pointer;
  transition: text-shadow 0.3s, transform 0.3s ease;
}
#nav-logo:hover,
#nav-logo:active {
  transform: scale(1.03);
  text-shadow: var(--basic-shadow-s);
}
#nav-btn-container {
  height: 40px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.icon-btn {
  margin: 5px 10px;
  transition: transform 0.3s ease;
}
.icon-btn:hover,
.icon-btn:active {
  transform: scale(1.2);
}

#icon-profile {
  width: 40px;
  height: 40px;
  padding: 2px;
  box-sizing: content-box;
  margin: 5px 10px;
  cursor: pointer;
  transition: transform 0.5s ease;
}
#icon-profile:hover,
#icon-profile:active {
  transform: scale(1.05);
}
@media (max-width: 426px) {
  #icon-profile {
    margin-top: 10px;
  }
}

#login-btn {
  width: 100px;
  height: 40px;
  margin: 5px 10px;
  font-size: 24px;
  border: solid 1px #000;
  transition: color 0.3s, background-color 0.3s ease;
}
#login-btn:hover,
#login-btn:active {
  background-color: #000 !important;
  color: #fff;
}

/* --------------------------------------- */
/* 아래는 모바일 헤더 */
/* --------------------------------------- */
#nav-container-mobile {
  display: none;
}
#nav-logo-m {
  width: 160px;
  padding: 8px;
  margin-left: 10px;
  text-align: center;
  font-size: 26px;
}
.m-btn {
  padding: 13px;
}
@media (max-width: 426px) {
  #nav-container-mobile {
    display: flex;
  }
}
#login-btn2 svg {
  width: 30px;
  height: 30px;
}
</style>
