import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FrotaFormService } from './frota-form.service';
import { FrotaService } from '../service/frota.service';
import { IFrota } from '../frota.model';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { PerfilService } from 'app/entities/perfil/service/perfil.service';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { CategoriaVeiculoService } from 'app/entities/categoria-veiculo/service/categoria-veiculo.service';

import { FrotaUpdateComponent } from './frota-update.component';

describe('Frota Management Update Component', () => {
  let comp: FrotaUpdateComponent;
  let fixture: ComponentFixture<FrotaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let frotaFormService: FrotaFormService;
  let frotaService: FrotaService;
  let perfilService: PerfilService;
  let categoriaVeiculoService: CategoriaVeiculoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FrotaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FrotaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FrotaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    frotaFormService = TestBed.inject(FrotaFormService);
    frotaService = TestBed.inject(FrotaService);
    perfilService = TestBed.inject(PerfilService);
    categoriaVeiculoService = TestBed.inject(CategoriaVeiculoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Perfil query and add missing value', () => {
      const frota: IFrota = { id: 456 };
      const perfil: IPerfil = { id: 5982 };
      frota.perfil = perfil;

      const perfilCollection: IPerfil[] = [{ id: 98815 }];
      jest.spyOn(perfilService, 'query').mockReturnValue(of(new HttpResponse({ body: perfilCollection })));
      const additionalPerfils = [perfil];
      const expectedCollection: IPerfil[] = [...additionalPerfils, ...perfilCollection];
      jest.spyOn(perfilService, 'addPerfilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frota });
      comp.ngOnInit();

      expect(perfilService.query).toHaveBeenCalled();
      expect(perfilService.addPerfilToCollectionIfMissing).toHaveBeenCalledWith(
        perfilCollection,
        ...additionalPerfils.map(expect.objectContaining)
      );
      expect(comp.perfilsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CategoriaVeiculo query and add missing value', () => {
      const frota: IFrota = { id: 456 };
      const categoriaVeiculo: ICategoriaVeiculo = { id: 64081 };
      frota.categoriaVeiculo = categoriaVeiculo;

      const categoriaVeiculoCollection: ICategoriaVeiculo[] = [{ id: 58257 }];
      jest.spyOn(categoriaVeiculoService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaVeiculoCollection })));
      const additionalCategoriaVeiculos = [categoriaVeiculo];
      const expectedCollection: ICategoriaVeiculo[] = [...additionalCategoriaVeiculos, ...categoriaVeiculoCollection];
      jest.spyOn(categoriaVeiculoService, 'addCategoriaVeiculoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frota });
      comp.ngOnInit();

      expect(categoriaVeiculoService.query).toHaveBeenCalled();
      expect(categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaVeiculoCollection,
        ...additionalCategoriaVeiculos.map(expect.objectContaining)
      );
      expect(comp.categoriaVeiculosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const frota: IFrota = { id: 456 };
      const perfil: IPerfil = { id: 62210 };
      frota.perfil = perfil;
      const categoriaVeiculo: ICategoriaVeiculo = { id: 7268 };
      frota.categoriaVeiculo = categoriaVeiculo;

      activatedRoute.data = of({ frota });
      comp.ngOnInit();

      expect(comp.perfilsSharedCollection).toContain(perfil);
      expect(comp.categoriaVeiculosSharedCollection).toContain(categoriaVeiculo);
      expect(comp.frota).toEqual(frota);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrota>>();
      const frota = { id: 123 };
      jest.spyOn(frotaFormService, 'getFrota').mockReturnValue(frota);
      jest.spyOn(frotaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frota }));
      saveSubject.complete();

      // THEN
      expect(frotaFormService.getFrota).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(frotaService.update).toHaveBeenCalledWith(expect.objectContaining(frota));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrota>>();
      const frota = { id: 123 };
      jest.spyOn(frotaFormService, 'getFrota').mockReturnValue({ id: null });
      jest.spyOn(frotaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frota: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frota }));
      saveSubject.complete();

      // THEN
      expect(frotaFormService.getFrota).toHaveBeenCalled();
      expect(frotaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrota>>();
      const frota = { id: 123 };
      jest.spyOn(frotaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(frotaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePerfil', () => {
      it('Should forward to perfilService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(perfilService, 'comparePerfil');
        comp.comparePerfil(entity, entity2);
        expect(perfilService.comparePerfil).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategoriaVeiculo', () => {
      it('Should forward to categoriaVeiculoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoriaVeiculoService, 'compareCategoriaVeiculo');
        comp.compareCategoriaVeiculo(entity, entity2);
        expect(categoriaVeiculoService.compareCategoriaVeiculo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
