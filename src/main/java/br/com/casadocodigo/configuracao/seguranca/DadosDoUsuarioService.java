package br.com.casadocodigo.configuracao.seguranca;

import br.com.casadocodigo.usuarios.Usuario;
import br.com.casadocodigo.usuarios.Usuarios;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DadosDoUsuarioService implements UserDetailsService {

	@Autowired
	private Usuarios usuarios;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarios.buscarPorEmail(email);

		if (usuario.isPresent()) {
			return new ResourceOwner(usuario.get());
		} else {
			throw new UsernameNotFoundException("usuario não autorizado");
		}
	}
}
