import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ofertas.test-samples';

import { OfertasFormService } from './ofertas-form.service';

describe('Ofertas Form Service', () => {
  let service: OfertasFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OfertasFormService);
  });

  describe('Service methods', () => {
    describe('createOfertasFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOfertasFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataPostagem: expect.any(Object),
            quantidade: expect.any(Object),
            tipoCarga: expect.any(Object),
            localizacaoOrigem: expect.any(Object),
            localizacaoDestino: expect.any(Object),
            dataColeta: expect.any(Object),
            dataEntrega: expect.any(Object),
            dataModificacao: expect.any(Object),
            dataFechamento: expect.any(Object),
            status: expect.any(Object),
            tipoOferta: expect.any(Object),
            tipoTransporte: expect.any(Object),
            destino: expect.any(Object),
            origem: expect.any(Object),
            perfil: expect.any(Object),
          })
        );
      });

      it('passing IOfertas should create a new form with FormGroup', () => {
        const formGroup = service.createOfertasFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataPostagem: expect.any(Object),
            quantidade: expect.any(Object),
            tipoCarga: expect.any(Object),
            localizacaoOrigem: expect.any(Object),
            localizacaoDestino: expect.any(Object),
            dataColeta: expect.any(Object),
            dataEntrega: expect.any(Object),
            dataModificacao: expect.any(Object),
            dataFechamento: expect.any(Object),
            status: expect.any(Object),
            tipoOferta: expect.any(Object),
            tipoTransporte: expect.any(Object),
            destino: expect.any(Object),
            origem: expect.any(Object),
            perfil: expect.any(Object),
          })
        );
      });
    });

    describe('getOfertas', () => {
      it('should return NewOfertas for default Ofertas initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOfertasFormGroup(sampleWithNewData);

        const ofertas = service.getOfertas(formGroup) as any;

        expect(ofertas).toMatchObject(sampleWithNewData);
      });

      it('should return NewOfertas for empty Ofertas initial value', () => {
        const formGroup = service.createOfertasFormGroup();

        const ofertas = service.getOfertas(formGroup) as any;

        expect(ofertas).toMatchObject({});
      });

      it('should return IOfertas', () => {
        const formGroup = service.createOfertasFormGroup(sampleWithRequiredData);

        const ofertas = service.getOfertas(formGroup) as any;

        expect(ofertas).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOfertas should not enable id FormControl', () => {
        const formGroup = service.createOfertasFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOfertas should disable id FormControl', () => {
        const formGroup = service.createOfertasFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
