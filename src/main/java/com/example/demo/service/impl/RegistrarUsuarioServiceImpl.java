package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.model.Telefono;
import com.example.demo.model.Usuario;
import com.example.demo.repository.TelefonoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.IRegistraUsuarioService;
import com.example.demo.util.JwtService;
import com.example.demo.util.PasswordEncoderService;

/**
 * Implementación de la interfaz IRegistraUsuarioService.
 * Esta clase proporciona la lógica de negocio para el registro de nuevos usuarios.
 * Utiliza los repositorios UsuarioRepository y TelefonoRepository para acceder a la base de datos,
 * el servicio PasswordEncoderService para codificar contraseñas y el servicio JwtService para generar tokens JWT.
 * @author [Marco Hermosilla]
 * @version 1.0
 * @since [03-04-2025]
 */
@Service
public class RegistrarUsuarioServiceImpl implements IRegistraUsuarioService {
	/**
     * Expresión regular para validar el formato del correo electrónico.
     * Se inyecta desde la configuración de la aplicación.
     */
    @Value("${email.regex}")
    private String emailRegex;

    /**
     * Expresión regular para validar el formato de la contraseña.
     * Se inyecta desde la configuración de la aplicación.
     */
    @Value("${password.regex}")
    private String passwordRegex;

    /**
     * Repositorio para acceder a la entidad Usuario en la base de datos.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Repositorio para acceder a la entidad Telefono en la base de datos.
     */
    private final TelefonoRepository telefonoRepository;

    /**
     * Servicio para codificar contraseñas.
     */
    private final PasswordEncoderService passwordEncoderService;

    /**
     * Servicio para generar tokens JWT.
     */
    private final JwtService jwtService;

	/**
	 * Constructor de la clase RegistrarUsuarioImpl.
	 *
	 * @param usuarioRepository       Repositorio para la entidad Usuario.
	 * @param telefonoRepository      Repositorio para la entidad Telefono.
	 * @param passwordEncoderService  Servicio para la codificación de contraseñas.
	 * @param jwtService              Servicio para la generación de tokens JWT.
	 */
	public RegistrarUsuarioServiceImpl(UsuarioRepository usuarioRepository, TelefonoRepository telefonoRepository,
			PasswordEncoderService passwordEncoderService, JwtService jwtService) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.telefonoRepository = telefonoRepository;
		this.passwordEncoderService = passwordEncoderService;
		this.jwtService = jwtService;
	}

	/**
	 * Registra un nuevo usuario en el sistema.
	 *
	 * @param registerDTO Datos del usuario a registrar.
	 * @return AuthResponse con la información del usuario registrado y su token.
	 */
	@Override
	public AuthResponseDTO registrarUsuario(RegisterDTO registerDTO) {
		Usuario usuario = UsuarioMapper.INSTANCE.registerDTOToUsuario(registerDTO);
		Usuario usuarioGuardado = guardarUsuario(usuario);
		guardarTelefonos(usuarioGuardado);
		return AuthResponseDTO.builder().id(usuarioGuardado.getId()).name(usuarioGuardado.getName())
				.email(usuarioGuardado.getEmail()).created(usuarioGuardado.getCreated())
				.lastLogin(usuarioGuardado.getLastLogin()).token(usuarioGuardado.getToken())
				.isActive(usuarioGuardado.isActive()).build();
	}

	/**
	 * Guarda un usuario en la base de datos después de validar y procesar sus datos.
	 *
	 * @param usuario Usuario a guardar.
	 * @return Usuario guardado en la base de datos.
	 */
	private Usuario guardarUsuario(Usuario usuario) {
		validarUsuario(usuario);
		String jwtToken = jwtService.generarToken(usuario.getEmail());
		usuario.setPassword(passwordEncoderService.encodePassword(usuario.getPassword()));
		usuario.setCreated(LocalDateTime.now());
		usuario.setLastLogin(LocalDateTime.now());
		usuario.setToken(jwtToken);
		usuario.setActive(true);
		return usuarioRepository.save(usuario);
	}
	
	/**
	 * Guarda los teléfonos asociados a un usuario en la base de datos.
	 *
	 * @param usuario Usuario al que pertenecen los teléfonos.
	 */
	private void guardarTelefonos(Usuario usuario) {
		List<Telefono> telefonos = usuario.getPhones();
		if (telefonos != null) {
			telefonos.forEach(telefono -> telefono.setUsuario(usuario));
			telefonoRepository.saveAll(telefonos);
		}
	}

	/**
	 * Valida los datos del usuario antes de guardarlo en la base de datos.
	 *
	 * @param usuario Usuario a validar.
	 * @throws IllegalArgumentException si el formato del correo o la contraseña es inválido.
	 * @throws UserAlreadyExistsException si el correo ya está registrado.
	 */
	private void validarUsuario(Usuario usuario) {
		// Validar correo
		if (!Pattern.matches(emailRegex, usuario.getEmail())) {
			throw new IllegalArgumentException("Formato de correo electrónico inválido");
		}
		// Validar contraseña
		if (!Pattern.matches(passwordRegex, usuario.getPassword())) {
			throw new IllegalArgumentException("Formato de contraseña inválido");
		}
		// Validar Usuario Existe
		Usuario buscarUsuario = usuarioRepository.findByEmail(usuario.getEmail());
		if (buscarUsuario != null) {
			throw new UserAlreadyExistsException("El correo ya registrado");
		}
	}

}
