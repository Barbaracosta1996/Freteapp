import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'perfil',
        data: { pageTitle: 'Perfils' },
        loadChildren: () => import('./perfil/perfil.module').then(m => m.PerfilModule),
      },
      {
        path: 'perfil-anexos',
        data: { pageTitle: 'PerfilAnexos' },
        loadChildren: () => import('./perfil-anexos/perfil-anexos.module').then(m => m.PerfilAnexosModule),
      },
      {
        path: 'ofertas',
        data: { pageTitle: 'Ofertas' },
        loadChildren: () => import('./ofertas/ofertas.module').then(m => m.OfertasModule),
      },
      {
        path: 'solicitacao',
        data: { pageTitle: 'Solicitacaos' },
        loadChildren: () => import('./solicitacao/solicitacao.module').then(m => m.SolicitacaoModule),
      },
      {
        path: 'categoria-veiculo',
        data: { pageTitle: 'CategoriaVeiculos' },
        loadChildren: () => import('./categoria-veiculo/categoria-veiculo.module').then(m => m.CategoriaVeiculoModule),
      },
      {
        path: 'frota',
        data: { pageTitle: 'Frotas' },
        loadChildren: () => import('./frota/frota.module').then(m => m.FrotaModule),
      },
      {
        path: 'rotas-ofertas',
        data: { pageTitle: 'RotasOfertas' },
        loadChildren: () => import('./rotas-ofertas/rotas-ofertas.module').then(m => m.RotasOfertasModule),
      },
      {
        path: 'whats-message-batch',
        data: { pageTitle: 'WhatsMessageBatches' },
        loadChildren: () => import('./whats-message-batch/whats-message-batch.module').then(m => m.WhatsMessageBatchModule),
      },
      {
        path: 'settings-contracts',
        data: { pageTitle: 'SettingsContracts' },
        loadChildren: () => import('./settings-contracts/settings-contracts.module').then(m => m.SettingsContractsModule),
      },
      {
        path: 'settings-carga-app',
        data: { pageTitle: 'SettingsCargaApps' },
        loadChildren: () => import('./settings-carga-app/settings-carga-app.module').then(m => m.SettingsCargaAppModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
