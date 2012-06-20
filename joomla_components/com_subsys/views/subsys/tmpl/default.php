<?php defined('_JEXEC') or die('Restricted access'); 
JHTML::_('behavior.tooltip');
?>
<form action="index.php" method="post" name="adminForm">
<div id="editcell">
<table>
<tr>
	  <td align="left" width="100%">
				<?php echo JText::_( 'Filter' ); ?>:
				<input type="text" name="search" id="search" value="<?php echo htmlspecialchars($this->lists['search']);?>" class="text_area" onchange="document.adminForm.submit();" />
				<button onclick="this.form.submit();"><?php echo JText::_( 'Go' ); ?></button>
				<button onclick="document.getElementById('search').value='';this.form.submit();"><?php echo JText::_( 'Reset' ); ?></button>
			</td>
			
	</tr>
</table>
	<table class="adminlist">
	<thead>
		<tr>
			<th width="5">
				<?php echo JText::_( 'ID' ); ?>
			</th>
			<th width="20">
				<input type="checkbox" name="toggle" value="" onclick="checkAll(<?php echo count( $this->items ); ?>);" />
			</th>			
			<th>
				<?php echo JText::_( 'Subscriber Code' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Subscriber Name' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Category' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Address' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'City' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'POBox' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Phone' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Fax' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Email' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'WebSite' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Contact Person' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Designation' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Created Date' ); ?>
			</th>
		</tr>
	</thead>
	<?php
	$k = 0;
	for ($i=0, $n=count( $this->items ); $i < $n; $i++)	{
		$row =& $this->items[$i];
		$checked 	= JHTML::_('grid.id',   $i, $row->sub_id );
		$link 		= JRoute::_( 'index.php?option=com_subsys&controller=subscriber&task=edit&cid[]='. $row->sub_id );		
		?>
		<tr class="<?php echo "row$k"; ?>">
			<td>
				<?php echo $row->sub_id; ?>
			</td>
			<td>
				<?php echo $checked; ?>
			</td>
			<td>
				<?php echo $row->sub_code; ?>
			</td>
			<td>
				<a href="<?php echo $link; ?>"><?php echo $row->sub_name; ?></a>
			</td>
			<td>
				<?php echo $row->sub_category; ?>
			</td>
			<td>
				<?php echo $row->sub_address; ?>
			</td>
			<td>
				<?php echo $row->sub_city; ?>
			</td>
			<td>
				<?php echo $row->sub_pobox; ?>
			</td>
			<td>
				<?php echo $row->sub_phone; ?>
			</td>
			<td>
				<?php echo $row->sub_fax; ?>
			</td>
			<td>
			<a href="mailto:<?php echo $row->sub_email; ?>"><?php echo $row->sub_email; ?></a>
			</td>
			<td>
			<a href="http://<?php echo $row->sub_web; ?>" target="_blank"><?php echo $row->sub_web; ?></a>				
			</td>
			<td>
				<?php echo $row->sub_cp; ?>
			</td>
			<td>
				<?php echo $row->sub_cpd; ?>
			</td>
			<td>
				<?php echo $row->cdate; ?>
			</td>
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>
	<tfoot>
    <tr>
      <td colspan="15"><?php echo $this->pagination->getListFooter(); ?></td>
    </tr>
  </tfoot>
	</table>
</div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="boxchecked" value="0" />
<input type="hidden" name="controller" value="subscriber" />
<input type="hidden" name="view" value="subsys" />
</form>
