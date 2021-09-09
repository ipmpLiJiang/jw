export default function routeReplaceSelf(component) {
  return {
    name: 'routeReplaceSelf',
    computed: {
      showChild() {
        const deepestMatchedRoute = this.$route.matched[this.$route.matched.length - 1];
        return deepestMatchedRoute.instances.default !== this;
      },
    },
    render(h) {
      const child = this.showChild ? h('router-view') : h(component);
      return h('keep-alive', [child]);
    },
  };
}
