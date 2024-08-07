import DashboardLayout from '@/views/Layout/DashboardLayout.vue';
import AuthLayout from '@/views/Pages/AuthLayout.vue';

import NotFound from '@/views/NotFoundPage.vue';

const routes = [
  {
    path: '/',
    redirect: 'dashboard',
    component: DashboardLayout,
    children: [
      {
        path: '/dashboard',
        name: 'dashboard',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "demo" */ '../views/Dashboard.vue')
      },
      {
        path: '/application',
        name: 'application',
        component: () => import(/* webpackChunkName: "demo" */ '../views/ApplicationAdm.vue')
      },
      {
        path: '/document',
        name: 'document',
        component: () => import(/* webpackChunkName: "demo" */ '../views/Document.vue')
      },
      {
        path: '/create-document',
        name: 'documentCreation',
        component: () => import(/* webpackChunkName: "demo" */ '../views/Document/DocumentForm.vue')
      },
      {
        path: '/modify-document/:id',
        name: 'documentModification',
        component: () => import(/* webpackChunkName: "demo" */ '../views/Document/DocumentForm.vue')
      },
      {
        path: '/associate-applications/:id',
        name: 'documentAssociation',
        component: () => import(/* webpackChunkName: "demo" */ '../views/DocumentApplication/DocumentApplicationAssociation.vue')
      },
      {
        path: '/document-data/:id',
        name: 'documentDataDeletion',
        component: () => import(/* webpackChunkName: "demo" */ '../views/DocumentData/DocumentDataTable.vue')
      },
      {
        path: '/file-creation',
        name: 'fileCreationManagement',
        component: () => import(/* webpackChunkName: "demo" */ '../views/FileCreationMain.vue')
      },
      {
        path: '/icons',
        name: 'icons',
        component: () => import(/* webpackChunkName: "demo" */ '../views/Icons.vue')
      },
      {
        path: '/profile',
        name: 'profile',
        component: () => import(/* webpackChunkName: "demo" */ '../views/Pages/UserProfile.vue')
      },
      {
        path: '/audit',
        name: 'audit',
        component: () => import(/* webpackChunkName: "demo" */ '../views/AuditTableMain.vue')
      }
    ]
  },
  {
    path: '/',
    redirect: 'login',
    component: AuthLayout,
    children: [
      {
        path: '/login',
        name: 'login',
        component: () => import(/* webpackChunkName: "demo" */ '../views/Pages/Login.vue')
      },
      {
        path: '/register',
        name: 'register',
        component: () => import(/* webpackChunkName: "demo" */ '../views/Pages/Register.vue')
      },
      { path: '*', component: NotFound }
    ]
  }
];

export default routes;
