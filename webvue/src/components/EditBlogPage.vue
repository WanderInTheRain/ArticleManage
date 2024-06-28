<template>
  <el-container v-if="isLoggedIn">
    <el-header>
      <el-row  v-if="this.query==='edit'">
        <el-col :span="16" style="display: flex; justify-content: center;vertical-align: center;
         align-items: center;margin-right: 20px;margin-top: 20px;">
          <el-input v-model="this.blog.title" placeholder="请输入标题" style="font-size: 20px;"></el-input>
        </el-col>
        <el-col :span="7" style="display: flex; justify-content: center; align-items: center;margin-top: 20px;">
          <el-button type="primary" round @click="getauthandsaveedit">
            <el-icon><Edit /></el-icon>保存文章
          </el-button>
          <el-button type="danger" round @click="deletearticle">
            <el-icon><Delete /></el-icon>删除文章
          </el-button>
          <el-button type="danger" round @click="backhome">
            <el-icon><CloseBold /></el-icon>回到主页
          </el-button>
        </el-col>
      </el-row>
      <el-row v-else>
        <el-col :span="19" style="display: flex; justify-content: center;vertical-align: center;
         align-items: center;margin-right: 20px;margin-top: 20px;">
          <el-input v-model="this.blog.title" placeholder="请输入标题" style="font-size: 20px;"></el-input>
        </el-col>
        <el-col :span="4" style="display: flex; justify-content: center; align-items: center;margin-top: 20px;">
          <el-button type="primary" round @click="getauthandsave">
            <el-icon><Edit /></el-icon>保存文章
          </el-button>
          <el-button type="danger" round @click="backhome">
            <el-icon><CloseBold /></el-icon>回到主页
          </el-button>
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-row style="margin-bottom: 20px">
        <el-col :span="5" style="margin-right: 20px">
          <el-tree-select
              v-model="value"
              :data="data"
              check-strictly
              :render-after-expand="false"
              style="width: 240px"
          />
        </el-col>
        <el-col :span="16">
          <el-button type="primary" round @click="dialogFormVisible = true">
            添加分类
          </el-button>

          <el-dialog v-model="dialogFormVisible" title="添加分类" width="300px">
            <el-form>
              <el-form-item label="新分类名称" label-width="100px">
                <el-input v-model="name" autocomplete="off" style="width: 240px"/>
              </el-form-item>
              <el-form-item label="父分类" label-width="100px">
                <el-tree-select
                    v-model="newvalue"
                    :data="data"
                    check-strictly
                    :render-after-expand="false"
                    style="width: 240px"
                />
              </el-form-item>
            </el-form>
            <div style="text-align: right">
              <el-button type="primary" @click="addcategory">
                确定
              </el-button>
            </div>
          </el-dialog>
        </el-col>
        <el-col :span="2">
          <el-checkbox v-model="this.share">是否公开</el-checkbox>
        </el-col>
      </el-row>
      <div ref="editor" class="editor-container"></div>
      <el-dialog
          v-model="dialogVisible"
          width="500"
          :show-close="false"
      >
        <el-result
            icon="success"
            title="保存成功"
        ></el-result>
      </el-dialog>
      <el-dialog
          v-model="deleteVisible"
          width="500"
          :show-close="false"
      >
        <el-result
            icon="success"
            title="删除成功"
        ></el-result>
      </el-dialog>
      <el-dialog
          v-model="authlow"
          width="500"
          :show-close="false"
      >
        <el-result
            icon="error"
            title="权限不足"
        ></el-result>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
import Quill from 'quill';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.core.css';
import axios from 'axios';

import { ref } from 'vue'

export default {
  name: 'EditBlogPage',
  data() {
    return {
      quill: null,
      message: '',
      savedContent: '',
      articleId: '',
      blog: {
        title: '',
        content: '',
        categoryid: 0,
        share: 1
      },
      data: [
        {
          value: '1',
          label: 'Level one 1',
          children: [
            {
              value: '1-1',
              label: 'Level two 1-1',
              children: [],
            },
          ],
        }],
      newCategory: '',
      dialogFormVisible: false,
      dialogVisible: false,
      deleteVisible: false,
      name: '',
      gridData: [],
      value: '0',
      newvalue: '0',
      userid: '',
      share: true,
      query: 'create',
      authlow: false
    };
  },
  computed: {
    isLoggedIn() {
      return sessionStorage.getItem('session') !== null;
    }
  },
  mounted() {
    this.dialogFormVisible = ref(false)

    if (this.isLoggedIn) {
      this.$nextTick(() => {
        this.initializeQuillEditors();
      });
    }
  },
  methods: {
    initializeQuillEditors() {
      const toolbarOptions = [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{'header': 1}, {'header': 2}],
        [{'list': 'ordered'}, {'list': 'bullet'}],
        [{'script': 'sub'}, {'script': 'super'}],
        [{'indent': '-1'}, {'indent': '+1'}],
        [{'direction': 'rtl'}],
        [{'size': ['small', false, 'large', 'huge']}],
        [{'header': [1, 2, 3, 4, 5, 6, false]}],
        [{'color': []}, {'background': []}],
        [{'font': []}],
        [{'align': []}],
        ['clean'],
        ['image']
      ];

      this.quill = new Quill(this.$refs.editor, {
        theme: 'snow',
        modules: {
          toolbar: {
            container: toolbarOptions,
            handlers: {
              image: this.imageHandler
            }
          },
          imageResize: {
            module: 'imageResize',
            handleStyles: {
              backgroundColor: 'black',
              border: 'none',
              color: 'white',
              cursor: 'pointer',
              display: 'block',
              fontFamily: 'Arial, sans-serif',
              fontSize: '12px',
              padding: '3px',
              position: 'absolute',
              right: '0',
              top: '0'
            }
          }
        }
      });
      this.getcategorytree();
      this.getuserid();
      console.log('query is ' + this.query)
      this.query = this.$route.params.query;
      if (this.query === 'edit') {
        this.initblog();
      }
    },
    imageHandler() {
      const input = document.createElement('input');
      input.setAttribute('type', 'file');
      input.setAttribute('accept', 'image/jpeg, image/jpg, image/png');
      input.click();

      input.onchange = () => {
        const file = input.files[0];
        if (file) {
          const formData = new FormData();
          formData.append('image', file);

          axios.post('https://frp-fun.top:52702/article/imgsave', formData)
          .then(response => {
            this.$nextTick(() => {
              const imageUrl = response.data.url;
              this.quill.root.innerHTML += `<img src="${imageUrl}" alt='image'>`;
            });
          })
          .catch(error => {
            console.error('Error uploading image:', error);
          });
        }
      };
    },
    getauthandsave(){
      axios.post('https://frp-fun.top:52702/user/getuserauth', null, {params: {userid: this.userid}})
      .then(response => {
        const auth = response.data;
        console.log('auths ' + auth);
        if (auth >= 1){
          this.saveContent();
        }
        else{
          this.authmsg();
        }
      })
      .catch(error => {
        console.error('Error checking login status:', error);
      });
    },
    saveContent() {
      const content = this.quill.root.innerHTML;

      const params = {
        authorid: this.userid,
        title: this.blog.title,
        content: content,
        share: this.share ? 1 : 0,
        categoryid: this.value,
        key: ""
      };

      axios.post('https://frp-fun.top:52702/article/createarticle', null, {params})
      .then(response => {
        console.log(response.data);
        const ifsucc = response.data;
        if (ifsucc === 'success'){
          this.sucessmsg();
        }
      })
      .catch(error => {
        console.error('Error saving data:', error);
      });
    },
    getauthandsaveedit(){
      axios.post('https://frp-fun.top:52702/user/getuserauth', null, {params: {userid: this.userid}})
      .then(response => {
        const auth = response.data;
        if (auth >= 1){
          this.saveContentedit();
        }
        else{
          this.authmsg();
        }
      })
      .catch(error => {
        console.error('Error checking login status:', error);
      });
    },
    saveContentedit() {
      const content = this.quill.root.innerHTML;

      const params = {
        articleid: this.articleId,
        authorid: this.userid,
        title: this.blog.title,
        content: content,
        share: this.share ? 1 : 0,
        categoryid: this.value,
        key: ""
      };
      console.log('p is ' + params);
      axios.post('https://frp-fun.top:52702/article/savearticle', null, {params})
      .then(response => {
        console.log(response.data);
        const ifsucc = response.data;
        if (ifsucc === 'success'){
          this.sucessmsg();
        }
      })
      .catch(error => {
        console.error('Error saving data:', error);
      });
    },
    getcategorytree() {
      axios.post('https://frp-fun.top:52702/article/getcategorytree')
      .then(response => {
        const proxy = response.data;
        this.data = proxy.children.map(child => ({
          value: child.value,
          label: child.label,
          children: child.children
        }));
        this.data.push({value: '0',label: '所有',children: []})

        console.log(this.data);
      })
      .catch(error => {
        console.error('Error uploading image:', error);
      });
    },
    getuserid() {
      const sessionId = sessionStorage.getItem('session');
      console.log('session is ' + sessionId);
      if (sessionId && sessionId !== '-1') {
        axios.post('https://frp-fun.top:52702/user/test', null, {params: {sessionId}})
        .then(response => {
          this.userid = response.data;
          this.searchMyBlog(response.data);
          console.log('userid is ' + this.userid);
        })
        .catch(error => {
          console.error('Error checking login status:', error);
        });
      } else {
        console.error('Invalid session ID.');
      }
    },
    backhome() {
      this.$router.push('/home');
    },
    addcategory() {
      const params = {
        name: this.name,
        parentid: this.newvalue
      };
      axios.post('https://frp-fun.top:52702/article/addcategory', null, {params})
      .then(response => {
        console.log('new id is ' + response.data);
        this.getcategorytree();
        this.dialogFormVisible = false;
      })
      .catch(error => {
        console.error('Error saving data:', error);
      });
    },
    initblog(){
      this.articleId = this.$route.params.articleId;
      const params = {
        articleId: this.articleId,
      };
      console.log('articleid is ' + this.articleId);
      axios.post('https://frp-fun.top:52702/article/getblogbyid', null, {params})
      .then(response => {
        console.log('new id is ' + response.data);
        this.blog = response.data;
        console.log('sblog is ' + this.blog.share);
        this.value = this.blog.categoryid.toString();
        this.quill.root.innerHTML = this.blog.content;
        if (this.blog.share === 1){
          this.share = true;
        }
        else{
          this.share = false;
        }
        console.log('cblog is ' + this.value);
      })
      .catch(error => {
        console.error('Error saving data:', error);
      });
    },
    sucessmsg(){
      this.dialogVisible = true;
      setTimeout(() => {
        this.dialogVisible = false;
      }, 500); // 0.5 seconds
    },
    deletearticle(){
      const params = {
        articleid: this.articleId
      };
      axios.post('https://frp-fun.top:52702/article/deletearticle', null, {params})
      .then(response => {
        console.log('new id is ' + response.data);
        const ifsucc = response.data;
        if (ifsucc === 'success'){
          this.deletemsg();
        }
      })
      .catch(error => {
        console.error('Error saving data:', error);
      });
    },
    deletemsg(){
      this.deleteVisible = true;
      setTimeout(() => {
        this.deleteVisible = false;
        this.$router.push('/home');
      }, 500); // 0.5 seconds
    },
    authmsg(){
      this.authlow = true;
      setTimeout(() => {
        this.authlow = false;
      }, 500); // 0.5 seconds
    }
  }
};
</script>

<style>
.editor-container {
  min-height: 200px; /* Adjust height as needed */
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-top: 10px;
}
</style>
