<template>
  <div>
    <div
      class="excellent"
      v-if="level == 1"
    >
    <img src="../../assets/img/titleExcellent.png" alt="">
    </div>
    <div
      class="good"
      v-else-if="level == 2"
    >
    <img src="../../assets/img/titleGood.png" alt="">
    </div>
    <div
      class="ordinary"
      v-else-if="level == 3"
    >
     <img src="../../assets/img/titleordinary.png" alt=""></div>
    <div
      class="bad"
      v-else-if="level == 4"
    >
    <img src="../../assets/img/titlebad.png" alt="">
    </div>
    <div
      class="none"
      v-else
    >未评分</div>
  </div>
</template>
<script>
import { Formlist, mySbmit, editById } from "@/views/ydyf/api/Form/index";
import qs from "qs";
export default {
  data() {
    return {};
  },
  props: ["level"],
  components: {},
  mounted() {},
  created() {},
  methods: {},
};
</script>


<style lang="scss" scoped>
.excellent {
  width: 8rem;
  height: 8rem;
  border-radius: 50%;
  // border: 2px solid #2ade31;
  position: absolute;
  text-align: center;
  line-height: 8rem;
  z-index: 999;
  // transform: rotate(30deg);
  color: #2ade31;
  font-size: 30px;
  right: 3rem;
  top: 11rem;
  font-weight: bold;
    img{
    width: 110%;
    height: 110%;
  }
}
.good {
  width: 8rem;
  height: 8rem;
  border-radius: 50%;
  // border: 2px solid #409eff;
  position: absolute;
  text-align: center;
  line-height: 8rem;
  z-index: 999;
  // transform: rotate(30deg);
  color: #409eff;
  font-size: 30px;
  right: 3rem;
  top: 11rem;
  font-weight: bold;
    img{
    width: 110%;
    height: 110%;
  }
}
.ordinary {
  width: 8rem;
  height: 8rem;
  border-radius: 50%;
  // border: 2px solid #e6a23c;
  position: absolute;
  text-align: center;
  line-height: 8rem;
  z-index: 999;
  // transform: rotate(30deg);
  color: #e6a23c;
  font-size: 30px;
  right: 3rem;
  top: 11rem;
  font-weight: bold;
    img{
    width: 110%;
    height: 110%;
  }
}
.bad {
  width: 8rem;
  height: 8rem;
  border-radius: 50%;
  // border: 2px solid #f56c6c;
  position: absolute;
  text-align: center;
  line-height: 8rem;
  z-index: 999;
  // transform: rotate(30deg);
  color: #f56c6c;
  font-size: 30px;
  right: 3rem;
  top: 11rem;
  font-weight: bold;
    img{
    width: 110%;
    height: 110%;
  }
}
.none{
    width: 8rem;
  height: 8rem;
  border-radius: 50%;
  border: 2px solid #909399;
  position: absolute;
  text-align: center;
  line-height: 8rem;
  z-index: 999;
  transform: rotate(30deg);
  color: #909399;
  font-size: 30px;
  right: 3rem;
  top: 11rem;
  font-weight: bold;
}
</style>