<template>
  <div id="wrap">
    <div id="header" class="search-container">
      <p class="h-p l-desc" v-if="active === 'allPost'" @click="allPost">{{ header.allBoard }}</p>
      <p class="h-p m-desc">{{ getHeader }}</p>
      <p class="h-p l-desc" v-if="active === 'allBoard'" @click="allBoard">{{ header.allPost }}</p>

      <p class="h-p b-desc" style="padding-top: 2px;">|</p>
      <p class="h-p l-desc" :class="{ bold: getSort === sort.value[0] }" @click="clickSortNew">{{ sort.key[0] }}</p>
      <p class="h-p l-desc" :class="{ bold: getSort === sort.value[1] }" @click="clickSortHot">{{ sort.key[1] }}</p>
    </div>

    <div class="search-container">
      <div id="filter-container" v-if="getType">
        <button
          class="filter-btn l-desc"
          :class="{ newmorphism: select.index == index }"
          v-for="(item, index) in getType.key"
          :key="index"
          @click="clickFilter(index)"
        >
          {{ item }}
        </button>
      </div>
      <div class="search-container">
        <div id="search-bar" ref="searchBar">
          <input
            id="s-input"
            ref="sInput"
            @keyup.enter="onSearching"
            v-model="keyword"
            type="text"
            placeholder="검색어를 입력하세요"
          />
          <button id="s-del-btn" @click="clickDeleteBtn" :class="{ visible: keyword != '' }">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
              <path
                d="M12 0c-6.627 0-12 5.373-12 12s5.373 12 12 12 12-5.373 12-12-5.373-12-12-12zm4.151 17.943l-4.143-4.102-4.117 4.159-1.833-1.833 4.104-4.157-4.162-4.119 1.833-1.833 4.155 4.102 4.106-4.16 1.849 1.849-4.1 4.141 4.157 4.104-1.849 1.849z"
              />
            </svg>
          </button>
          <button id="s-btn" ref="searchBtn" @click="onSearching">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
              <path
                d="M23.809 21.646l-6.205-6.205c1.167-1.605 1.857-3.579 1.857-5.711 0-5.365-4.365-9.73-9.731-9.73-5.365 0-9.73 4.365-9.73 9.73 0 5.366 4.365 9.73 9.73 9.73 2.034 0 3.923-.627 5.487-1.698l6.238 6.238 2.354-2.354zm-20.955-11.916c0-3.792 3.085-6.877 6.877-6.877s6.877 3.085 6.877 6.877-3.085 6.877-6.877 6.877c-3.793 0-6.877-3.085-6.877-6.877z"
              />
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations, mapState } from 'vuex';
export default {
  name: 'Search',
  data() {
    return {
      select: {
        index: 0,
        type: '',
      },
      keyword: '',
      size: 10,
    };
  },
  computed: {
    ...mapGetters('search', ['getHeader', 'getType', 'getSort']),
    ...mapState('search', ['active', 'header', 'type', 'sort', 'epicenter']),
    ...mapState(['toastActive', 'toastType']),
  },
  mounted() {
    this.select.type = this.getType.value[0];
  },
  watch: {
    toastActive() {
      if (this.toastActive && this.toastType.search) {
        this.$refs.sInput.focus();
      }
    },
    // getIndex() {
    //   console.log(this.getIndex);
    //   console.log(this.getSort);
    // },
  },
  methods: {
    // ...mapActions('search', ['actSearchAllBoard', 'actSearchAllPost']),
    ...mapMutations('search', [
      'SET_SEARCH_ACTIVE',
      'SET_PAYLOAD_SORT',
      'SET_PAYLOAD',
      'CLEAR_SEARCH_LIST',
      'SET_PAYLOAD_ID',
    ]),
    //검색 관련
    clickCloser() {
      this.$store.commit('setToastTogle');
    },
    //search function
    clickFilter: function(index) {
      this.select.index = index;
      this.select.type = this.getType.value[index];
      var value = 54 * index;
      this.$refs.searchBar.style.marginTop = `${value}px`;
    },
    onSearching: function() {
      //네비게이션 바의 검색 아이콘을 눌러서 진입했을 때
      //분기 안해도 되는 것 (해당 페이지 내에서 검색버튼 눌렀을 때(active == epicenter))
      //메인의 경우 -> 전체 검색 페이지로 이동
      //페이지 이동
      this.SET_PAYLOAD({
        keyword: this.keyword,
        page: 0,
        type: this.select.type,
      });
      this.CLEAR_SEARCH_LIST();
      //숫자일 경우 보드 내 리스트 검색!
      if (!isNaN(this.epicenter)) {
        // this.SET_PAYLOAD_ID({ board_id: this.epicenter, user_id: this.$store.state.auth.user.userId });
        //모든 보드, 게시글 검색
      } else if (
        !(this.epicenter === '/board/search' && this.active === 'allBoard') &&
        !(this.epicenter === '/post/search' && this.active === 'allPost')
      ) {
        if (this.active === 'allBoard') {
          this.$router.push({ name: 'SearchBoard' });
        } else if (this.active === 'allPost') {
          this.$router.push({ name: 'SearchPost' });
        }
        //보드 전체검색에서 포스트 검색할 때
      } else if (this.epicenter === '/board/search' && this.active === 'allPost') {
        this.$router.push({ name: 'SearchPost' });
        //포스트 전체 검색에서 보드 검색할 때
      } else if (this.epicenter === '/post/search' && this.active === 'allBoard') {
        this.$router.push({ name: 'SearchBoard' });
      }

      this.$store.commit('setToastTogle');
    },

    clickDeleteBtn: function() {
      this.keyword = '';
      this.$refs.sInput.focus();
    },

    //전체 검색 전환
    allPost() {
      this.SET_SEARCH_ACTIVE({ active: 'allBoard', epicenter: this.epicenter });
      this.clickFilter(0);
    },
    allBoard() {
      this.SET_SEARCH_ACTIVE({ active: 'allPost', epicenter: this.epicenter });
      this.clickFilter(0);
    },

    //sort 전환
    clickSortNew() {
      this.SET_PAYLOAD_SORT('new');
    },
    clickSortHot() {
      this.SET_PAYLOAD_SORT('hot');
    },
  },
};
</script>

<style scoped>
#wrap {
  display: flex;
  flex-direction: column;
  align-content: center;
}
#header {
  text-align: center;
  font-size: 18px;
}
.h-p {
  padding: 5px;
  margin: 0 10px;
  cursor: pointer;
}
@media (max-width: 426px) {
  .h-p {
    padding: 0;
    margin: 0 6px;
  }
}
.bold {
  font-weight: 500 !important;
}
.search-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  width: 80%;
  max-width: 600px;
  margin: 0 auto;
  transition: all 0.6s ease;
}
#filter-container {
  display: flex;
  flex-direction: column;
}
.filter-btn {
  padding: 5px;
  margin: 10px 0;
  width: 100px;
  height: 34px;
  font-size: 15px;
  border-radius: 28px;
  border: #00000000 solid 1px;
  transition: box-shadow 0.6s ease;
}
.filter-btn:hover {
  background: #ebebe9;
  box-shadow: 10px 10px 20px #bcbcba, -10px -10px 20px #ffffff;
  transition: 0.3s all ease;
}

#search-bar {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  width: 100%;
  transition: all 0.6s ease;
}

#s-input {
  margin: 10px 10px;
  padding: 0 30px 0 10px;
  width: calc(100% - 50px);
  height: 34px;
  background-color: inherit;
  border: 1px solid #000;
  border-radius: 17px;
  transition: all 0.6s ease;
}

#s-del-btn {
  position: absolute;
  opacity: 0;
  margin: 13px 49px 0 0;
  fill: #00000040;
  transition: opacity 0.6s, fill 0.3s ease;
}
#s-del-btn:hover,
#s-del-btn:active {
  fill: #000000;
}
.visible {
  opacity: 1 !important;
}

#s-btn {
  margin: 10px 0 10px;
  width: 34px;
  height: 34px;
  border: solid 1px #000;
  border-radius: 64px;
  transition: fill 0.6s, background 0.6s ease;
}
#s-btn:hover,
#s-btn:active {
  background-color: #000 !important;
  fill: var(--basic-color-bg);
}
#s-btn svg {
  margin: -3px -1px 0 0;
  width: 18px;
  height: 18px;
}

#closer {
  position: absolute;
  top: 5px;
  right: 5px;
}

@media (max-width: 426px) {
  input::placeholder {
    font-size: 11px;
  }
}
</style>
