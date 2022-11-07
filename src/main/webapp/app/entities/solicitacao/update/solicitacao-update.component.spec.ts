import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SolicitacaoFormService } from './solicitacao-form.service';
import { SolicitacaoService } from '../service/solicitacao.service';
import { ISolicitacao } from '../solicitacao.model';
import { IOfertas } from 'app/entities/ofertas/ofertas.model';
import { OfertasService } from 'app/entities/ofertas/service/ofertas.service';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { PerfilService } from 'app/entities/perfil/service/perfil.service';

import { SolicitacaoUpdateComponent } from './solicitacao-update.component';

describe('Solicitacao Management Update Component', () => {
  let comp: SolicitacaoUpdateComponent;
  let fixture: ComponentFixture<SolicitacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let solicitacaoFormService: SolicitacaoFormService;
  let solicitacaoService: SolicitacaoService;
  let ofertasService: OfertasService;
  let perfilService: PerfilService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SolicitacaoUpdateComponent],
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
      .overrideTemplate(SolicitacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SolicitacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    solicitacaoFormService = TestBed.inject(SolicitacaoFormService);
    solicitacaoService = TestBed.inject(SolicitacaoService);
    ofertasService = TestBed.inject(OfertasService);
    perfilService = TestBed.inject(PerfilService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ofertas query and add missing value', () => {
      const solicitacao: ISolicitacao = { id: 456 };
      const ofertas: IOfertas = { id: 19486 };
      solicitacao.ofertas = ofertas;
      const minhaOferta: IOfertas = { id: 20761 };
      solicitacao.minhaOferta = minhaOferta;

      const ofertasCollection: IOfertas[] = [{ id: 27000 }];
      jest.spyOn(ofertasService, 'query').mockReturnValue(of(new HttpResponse({ body: ofertasCollection })));
      const additionalOfertas = [ofertas, minhaOferta];
      const expectedCollection: IOfertas[] = [...additionalOfertas, ...ofertasCollection];
      jest.spyOn(ofertasService, 'addOfertasToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ solicitacao });
      comp.ngOnInit();

      expect(ofertasService.query).toHaveBeenCalled();
      expect(ofertasService.addOfertasToCollectionIfMissing).toHaveBeenCalledWith(
        ofertasCollection,
        ...additionalOfertas.map(expect.objectContaining)
      );
      expect(comp.ofertasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Perfil query and add missing value', () => {
      const solicitacao: ISolicitacao = { id: 456 };
      const perfil: IPerfil = { id: 7098 };
      solicitacao.perfil = perfil;
      const requestedPerfil: IPerfil = { id: 7915 };
      solicitacao.requestedPerfil = requestedPerfil;

      const perfilCollection: IPerfil[] = [{ id: 46542 }];
      jest.spyOn(perfilService, 'query').mockReturnValue(of(new HttpResponse({ body: perfilCollection })));
      const additionalPerfils = [perfil, requestedPerfil];
      const expectedCollection: IPerfil[] = [...additionalPerfils, ...perfilCollection];
      jest.spyOn(perfilService, 'addPerfilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ solicitacao });
      comp.ngOnInit();

      expect(perfilService.query).toHaveBeenCalled();
      expect(perfilService.addPerfilToCollectionIfMissing).toHaveBeenCalledWith(
        perfilCollection,
        ...additionalPerfils.map(expect.objectContaining)
      );
      expect(comp.perfilsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const solicitacao: ISolicitacao = { id: 456 };
      const ofertas: IOfertas = { id: 41121 };
      solicitacao.ofertas = ofertas;
      const minhaOferta: IOfertas = { id: 59215 };
      solicitacao.minhaOferta = minhaOferta;
      const perfil: IPerfil = { id: 49105 };
      solicitacao.perfil = perfil;
      const requestedPerfil: IPerfil = { id: 94184 };
      solicitacao.requestedPerfil = requestedPerfil;

      activatedRoute.data = of({ solicitacao });
      comp.ngOnInit();

      expect(comp.ofertasSharedCollection).toContain(ofertas);
      expect(comp.ofertasSharedCollection).toContain(minhaOferta);
      expect(comp.perfilsSharedCollection).toContain(perfil);
      expect(comp.perfilsSharedCollection).toContain(requestedPerfil);
      expect(comp.solicitacao).toEqual(solicitacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolicitacao>>();
      const solicitacao = { id: 123 };
      jest.spyOn(solicitacaoFormService, 'getSolicitacao').mockReturnValue(solicitacao);
      jest.spyOn(solicitacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solicitacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: solicitacao }));
      saveSubject.complete();

      // THEN
      expect(solicitacaoFormService.getSolicitacao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(solicitacaoService.update).toHaveBeenCalledWith(expect.objectContaining(solicitacao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolicitacao>>();
      const solicitacao = { id: 123 };
      jest.spyOn(solicitacaoFormService, 'getSolicitacao').mockReturnValue({ id: null });
      jest.spyOn(solicitacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solicitacao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: solicitacao }));
      saveSubject.complete();

      // THEN
      expect(solicitacaoFormService.getSolicitacao).toHaveBeenCalled();
      expect(solicitacaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolicitacao>>();
      const solicitacao = { id: 123 };
      jest.spyOn(solicitacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solicitacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(solicitacaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOfertas', () => {
      it('Should forward to ofertasService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ofertasService, 'compareOfertas');
        comp.compareOfertas(entity, entity2);
        expect(ofertasService.compareOfertas).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePerfil', () => {
      it('Should forward to perfilService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(perfilService, 'comparePerfil');
        comp.comparePerfil(entity, entity2);
        expect(perfilService.comparePerfil).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
